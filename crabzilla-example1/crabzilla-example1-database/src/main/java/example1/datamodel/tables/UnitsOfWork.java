/*
 * This file is generated by jOOQ.
*/
package example1.datamodel.tables;


import example1.datamodel.Example1db;
import example1.datamodel.Keys;
import example1.datamodel.tables.records.UnitsOfWorkRecord;
import org.jooq.*;
import org.jooq.impl.TableImpl;

import javax.annotation.Generated;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.List;


/**
 * This class is generated by jOOQ.
 */
@Generated(
    value = {
        "http://www.jooq.org",
        "jOOQ version:3.9.2"
    },
    comments = "This class is generated by jOOQ"
)
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class UnitsOfWork extends TableImpl<UnitsOfWorkRecord> {

    private static final long serialVersionUID = -1785246036;

    /**
     * The reference instance of <code>example1db.units_of_work</code>
     */
    public static final UnitsOfWork UNITS_OF_WORK = new UnitsOfWork();

    /**
     * The class holding records for this type
     */
    @Override
    public Class<UnitsOfWorkRecord> getRecordType() {
        return UnitsOfWorkRecord.class;
    }

    /**
     * The column <code>example1db.units_of_work.uow_seq_number</code>.
     */
    public final TableField<UnitsOfWorkRecord, Long> UOW_SEQ_NUMBER = createField("uow_seq_number", org.jooq.impl.SQLDataType.BIGINT.nullable(false), this, "");

    /**
     * The column <code>example1db.units_of_work.uow_id</code>.
     */
    public final TableField<UnitsOfWorkRecord, String> UOW_ID = createField("uow_id", org.jooq.impl.SQLDataType.VARCHAR.length(36).nullable(false), this, "");

    /**
     * The column <code>example1db.units_of_work.uow_events</code>.
     */
    public final TableField<UnitsOfWorkRecord, Object> UOW_EVENTS = createField("uow_events", org.jooq.impl.DefaultDataType.getDefaultDataType("json"), this, "");

    /**
     * The column <code>example1db.units_of_work.cmd_id</code>.
     */
    public final TableField<UnitsOfWorkRecord, String> CMD_ID = createField("cmd_id", org.jooq.impl.SQLDataType.VARCHAR.length(36).nullable(false), this, "");

    /**
     * The column <code>example1db.units_of_work.cmd_data</code>.
     */
    public final TableField<UnitsOfWorkRecord, Object> CMD_DATA = createField("cmd_data", org.jooq.impl.DefaultDataType.getDefaultDataType("json"), this, "");

    /**
     * The column <code>example1db.units_of_work.ar_name</code>.
     */
    public final TableField<UnitsOfWorkRecord, String> AR_NAME = createField("ar_name", org.jooq.impl.SQLDataType.VARCHAR.length(36).nullable(false), this, "");

    /**
     * The column <code>example1db.units_of_work.ar_id</code>.
     */
    public final TableField<UnitsOfWorkRecord, String> AR_ID = createField("ar_id", org.jooq.impl.SQLDataType.VARCHAR.length(36).nullable(false), this, "");

    /**
     * The column <code>example1db.units_of_work.version</code>.
     */
    public final TableField<UnitsOfWorkRecord, Long> VERSION = createField("version", org.jooq.impl.SQLDataType.BIGINT, this, "");

    /**
     * The column <code>example1db.units_of_work.inserted_on</code>.
     */
    public final TableField<UnitsOfWorkRecord, Timestamp> INSERTED_ON = createField("inserted_on", org.jooq.impl.SQLDataType.TIMESTAMP.nullable(false).defaultValue(org.jooq.impl.DSL.inline("CURRENT_TIMESTAMP", org.jooq.impl.SQLDataType.TIMESTAMP)), this, "");

    /**
     * Create a <code>example1db.units_of_work</code> table reference
     */
    public UnitsOfWork() {
        this("units_of_work", null);
    }

    /**
     * Create an aliased <code>example1db.units_of_work</code> table reference
     */
    public UnitsOfWork(String alias) {
        this(alias, UNITS_OF_WORK);
    }

    private UnitsOfWork(String alias, Table<UnitsOfWorkRecord> aliased) {
        this(alias, aliased, null);
    }

    private UnitsOfWork(String alias, Table<UnitsOfWorkRecord> aliased, Field<?>[] parameters) {
        super(alias, null, aliased, parameters, "");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Schema getSchema() {
        return Example1db.EXAMPLE1DB;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Identity<UnitsOfWorkRecord, Long> getIdentity() {
        return Keys.IDENTITY_UNITS_OF_WORK;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public UniqueKey<UnitsOfWorkRecord> getPrimaryKey() {
        return Keys.KEY_UNITS_OF_WORK_PRIMARY;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<UniqueKey<UnitsOfWorkRecord>> getKeys() {
        return Arrays.<UniqueKey<UnitsOfWorkRecord>>asList(Keys.KEY_UNITS_OF_WORK_PRIMARY, Keys.KEY_UNITS_OF_WORK_UOW_ID, Keys.KEY_UNITS_OF_WORK_CMD_ID);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public UnitsOfWork as(String alias) {
        return new UnitsOfWork(alias, this);
    }

    /**
     * Rename this table
     */
    @Override
    public UnitsOfWork rename(String name) {
        return new UnitsOfWork(name, null);
    }
}