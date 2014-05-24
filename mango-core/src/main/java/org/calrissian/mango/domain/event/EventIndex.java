package org.calrissian.mango.domain.event;

import org.calrissian.mango.domain.TemporalIdentifiable;

public class EventIndex implements TemporalIdentifiable {

    private String id;
    private long timestamp;

    public EventIndex(String id, long timestamp) {
        this.id = id;
        this.timestamp = timestamp;
    }

    public String getId() {
        return id;
    }

    public Long getTimestamp() {
        return timestamp;
    }
}
