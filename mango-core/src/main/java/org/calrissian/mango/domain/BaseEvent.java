package org.calrissian.mango.domain;

import java.util.UUID;

import static com.google.common.base.Preconditions.checkNotNull;
import static java.lang.System.currentTimeMillis;

public class BaseEvent extends AbstractTupleCollection implements Event {

  private String id;
  private long timestamp;

  public BaseEvent(String id, long timestamp) {
    checkNotNull(id);
    this.id = id;
    this.timestamp = timestamp;
  }

  public BaseEvent() {
    this.id = UUID.randomUUID().toString();
    this.timestamp = currentTimeMillis();
  }

  public String getId() {
    return id;
  }

  public long getTimestamp() {
    return timestamp;
  }
}
