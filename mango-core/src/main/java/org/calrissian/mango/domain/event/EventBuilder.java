package org.calrissian.mango.domain.event;

import org.calrissian.mango.domain.entity.EntityBuilder;

public class EventBuilder extends EntityBuilder {

    private long timestamp;

    public EventBuilder(String type, String id, long timestamp) {
        super(type, id);
        this.timestamp = timestamp;
    }

    @Override
    public Event build() {
        return new BaseEvent(type, id, timestamp, attributes);
    }
}
