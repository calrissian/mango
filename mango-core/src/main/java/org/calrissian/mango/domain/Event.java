package org.calrissian.mango.domain;

public interface Event extends TupleCollection {

  String getId();

  long getTimestamp();
}
