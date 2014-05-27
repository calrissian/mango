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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        EventIndex that = (EventIndex) o;

        if (timestamp != that.timestamp) return false;
        if (id != null ? !id.equals(that.id) : that.id != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (int) (timestamp ^ (timestamp >>> 32));
        return result;
    }

    @Override
    public String toString() {
        return "EventIndex{" +
                "id='" + id + '\'' +
                ", timestamp=" + timestamp +
                '}';
    }
}
