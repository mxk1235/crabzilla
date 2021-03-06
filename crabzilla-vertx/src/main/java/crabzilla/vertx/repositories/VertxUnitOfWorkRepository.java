package crabzilla.vertx.repositories;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectWriter;
import crabzilla.model.*;
import crabzilla.vertx.util.DbConcurrencyException;
import io.vertx.core.Handler;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.jdbc.JDBCClient;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import lombok.val;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import static crabzilla.vertx.repositories.VertxSqlHelper.*;

@Slf4j
public class VertxUnitOfWorkRepository {

  private static final String UOW_ID = "uow_id";
  private static final String UOW_EVENTS = "uow_events";
  private static final String CMD_DATA = "cmd_data";
  private static final String VERSION = "version";

  private final String aggregateRootName;
  private final JDBCClient client;

  private final TypeReference<List<Event>> eventsListTpe =  new TypeReference<List<Event>>() {};

  public VertxUnitOfWorkRepository(@NonNull Class<? extends AggregateRoot> aggregateRootName, @NonNull JDBCClient client) {
    this.aggregateRootName = aggregateRootName.getSimpleName();
    this.client = client;
  }

  public void get(@NonNull final UUID uowId, @NonNull final Handler<Optional<UnitOfWork>> handler) {

    val SELECT_UOW_BY_ID = "select * from units_of_work where uow_id =? ";
    val params = new JsonArray().add(uowId.toString());

    client.getConnection(getConn -> {

      if (getConn.failed()) {
        throw new RuntimeException(getConn.cause());
      }

      val sqlConn = getConn.result();

      queryWithParams(sqlConn, SELECT_UOW_BY_ID, params, rs -> {

        val rows = rs.getRows();

        if (rows.size() == 0 ) {
          handler.handle(Optional.empty());
        } else {
          for (JsonObject row : rows) {
            val command = Json.decodeValue(row.getString(CMD_DATA), Command.class);
            final List<Event> events = readEvents(row.getString(UOW_EVENTS));
            val uow = new UnitOfWork(UUID.fromString(row.getString(UOW_ID)), command,
                    new Version(row.getLong(VERSION)), events);
            handler.handle(Optional.of(uow));
          }
        }

        sqlConn.close(done -> {
          if (done.failed()) {
            throw new RuntimeException(done.cause());
          }
        });

      });

    });

  }

  public void selectAfterVersion(@NonNull final String id, @NonNull final Version version,
                                 @NonNull final Handler<SnapshotData> handler) {

    log.info("will load id [{}] after version [{}]", id, version.getValueAsLong());

    val SELECT_AFTER_VERSION = "select uow_events, version from units_of_work " +
            " where ar_id = ? " +
            "   and ar_name = ? " +
            "   and version > ? " +
            " order by version ";

    val params = new JsonArray().add(id).add(aggregateRootName).add(version.getValueAsLong());

    client.getConnection(getConn -> {

      if (getConn.failed()) {
        throw new RuntimeException(getConn.cause());
      }

      val sqlConn = getConn.result();

      queryStreamWithParams(sqlConn, SELECT_AFTER_VERSION, params, stream -> {

        val list = new ArrayList<SnapshotData>() ;

        stream
          .resultSetClosedHandler(v -> {
            // will ask to restart the stream with the new result set if any
            stream.moreResults();

          })
          .handler(row -> {

            val events = readEvents(row.getString(0));
            val snapshotData = new SnapshotData(new Version(row.getLong(1)), events);
            list.add(snapshotData);

          }).endHandler(event -> {

            log.info("found {} units of work for id {} and version > {}",
                    list.size(), id, version.getValueAsLong());

            val finalVersion = list.size() == 0 ? new Version(0) :
                                                  list.get(list.size() - 1).getVersion();

            final List<Event> flatMappedToEvents = list.stream()
                    .flatMap(sd -> sd.getEvents().stream()).collect(Collectors.toList());

            handler.handle(new SnapshotData(finalVersion, flatMappedToEvents));

            sqlConn.close(done -> {

              if (done.failed()) {
                throw new RuntimeException(done.cause());
              }

            });

          });

        });

      });
  }

  public void append(@NonNull final UnitOfWork unitOfWork, Handler<Either<Throwable, Long>> handler) {

    val SELECT_CURRENT_VERSION =
            "select max(version) as last_version from units_of_work where ar_id = ? and ar_name = ? ";

    val INSERT_UOW = "insert into units_of_work " +
            "(uow_id, uow_events, cmd_id, cmd_data, ar_id, ar_name, version) " +
            "values (?, ?, ?, ?, ?, ?, ?)";


    client.getConnection(conn -> {

      if (conn.failed()) {
        throw new RuntimeException(conn.cause());
      }

      val sqlConn = conn.result();

      // start a transaction 
      startTx(sqlConn, startTx -> {

        // check current version  // TODO also check if command was not already processed given the commandId

        val params1 = new JsonArray()
                .add(unitOfWork.targetId().getStringValue())
                .add(aggregateRootName);

        queryWithParams(sqlConn, SELECT_CURRENT_VERSION, params1, rs -> {

          Long currentVersion = rs.getRows().get(0).getLong("last_version");

          currentVersion = currentVersion == null ? 0L : currentVersion;

          log.info("Found version  {}", currentVersion);

          if (currentVersion != unitOfWork.getVersion().getValueAsLong() - 1) {

            val error = new DbConcurrencyException (
                    String.format("ar_id = [%s], current_version = %d, new_version = %d",
                            unitOfWork.targetId().getStringValue(),
                            currentVersion, unitOfWork.getVersion().getValueAsLong())) ;

            handler.handle(Eithers.left(error));

            // and close the connection
            sqlConn.close(done -> {
              if (done.failed()) {
                throw new RuntimeException(done.cause());
              }
            });

            return ;
          }

          // if version is OK, then insert

          val cmdAsJson = writeValueAsString(Json.mapper.writerFor(Command.class), unitOfWork.getCommand());
          val eventsAsJson = writeValueAsString(Json.mapper.writerFor(eventsListTpe), unitOfWork.getEvents());

          val params2 = new JsonArray()
                  .add(unitOfWork.getUnitOfWorkId().toString())
                  .add(eventsAsJson)
                  .add(unitOfWork.getCommand().getCommandId().toString())
                  .add(cmdAsJson)
                  .add(unitOfWork.targetId().getStringValue())
                  .add(aggregateRootName)
                  .add(unitOfWork.getVersion().getValueAsLong());

          updateWithParams(sqlConn, INSERT_UOW, params2, updateResult -> {

            handler.handle(Eithers.right(updateResult.getKeys().getLong(0)));

            // commit data
            commitTx(sqlConn, commitTrans -> {

              // and close the connection
              sqlConn.close(done -> {
                if (done.failed()) {
                  throw new RuntimeException(done.cause());
                }
              });
            });

          });

        });

      });

    });

    // TODO decide about to save scheduled commands here
    ////              uow.collectEvents().stream()
    ////            .filter(event -> event instanceof CommandSchedulingEvent) // TODO idempotency
    ////            .map(event -> (CommandSchedulingEvent) e)
    ////            .forEachOrdered(cs -> commandScheduler.schedule(commandId, cs));

  }

  String writeValueAsString(ObjectWriter writer, Object obj) {
    try {
      return writer.writeValueAsString(obj);
    } catch (JsonProcessingException e) {
      throw new RuntimeException("When writing to JSON", e);
    }
  }

  List<Event> readEvents(String eventsAsJson) {
    try {
      return Json.mapper.readerFor(eventsListTpe).readValue(eventsAsJson);
    } catch (IOException e) {
      throw new RuntimeException("When reading events list from JSON", e);
    }
  }

}