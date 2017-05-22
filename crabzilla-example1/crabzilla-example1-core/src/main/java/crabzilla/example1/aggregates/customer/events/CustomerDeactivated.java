package crabzilla.example1.aggregates.customer.events;

import crabzilla.model.Event;
import lombok.Value;

import java.time.LocalDateTime;

@Value
public class CustomerDeactivated implements Event {
  String reason;
  LocalDateTime when;
}