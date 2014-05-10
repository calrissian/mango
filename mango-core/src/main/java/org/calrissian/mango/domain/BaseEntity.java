package org.calrissian.mango.domain;

import static com.google.common.base.Preconditions.checkNotNull;

public class BaseEntity extends AbstractTupleCollection implements Entity {

  private String id;
  private String type;

  public BaseEntity(String type, String id) {
    checkNotNull(type);
    checkNotNull(id);
    this.id = id;
    this.type = type;
  }

  public String getId() {
    return id;
  }

  public String getType() {
    return type;
  }
}
