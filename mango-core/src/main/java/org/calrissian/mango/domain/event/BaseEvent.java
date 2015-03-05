/*
 * Copyright (C) 2015 The Calrissian Authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.calrissian.mango.domain.event;


import org.calrissian.mango.domain.entity.BaseEntity;

import static java.lang.System.currentTimeMillis;
import static java.util.UUID.randomUUID;

/**
 * Default implementation of {@link Event}
 */
public class BaseEvent extends BaseEntity implements Event {

    private final long timestamp; // in Millis

    /**
     * New event with random UUID and timestamp defaulted to current time
     */
    @Deprecated
    public BaseEvent() {
        this("", randomUUID().toString());
    }   // for backward compatibility

    /**
     * New event with ID. Timestamp defaults to current time.
     */
    public BaseEvent(String type, String id) {
        this(type, id, currentTimeMillis());
    }


    @Deprecated
    public BaseEvent(String id) {
        this("", id, currentTimeMillis());
    }

    /**
     * New store entry with ID and a timestamp
     */
    public BaseEvent(String type, String id, long timestamp) {
        super(type, id);
        this.timestamp = timestamp;
    }

    @Deprecated
    public BaseEvent(String id, long timestamp) {
        this("", id, timestamp);
    }

    /**
     * Copy constructor
     */
    public BaseEvent(Event event) {
        this(event.getType(), event.getId(), event.getTimestamp());
        putAll(event.getAttributes());
    }

    /**
     * {@inheritDoc}
     */
    public long getTimestamp() {
        return timestamp;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BaseEvent)) return false;
        if (!super.equals(o)) return false;

        BaseEvent baseEvent = (BaseEvent) o;

        if (timestamp != baseEvent.timestamp) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (int) (timestamp ^ (timestamp >>> 32));
        return result;
    }

    @Override
    public String toString() {
        return "BaseEvent{" +
                "type='" + getType() + '\'' +
                ", id='" + getId() + '\'' +
                ", timestamp=" + timestamp +
                ", tuples=" + getAttributes() +
                '}';
    }
}
