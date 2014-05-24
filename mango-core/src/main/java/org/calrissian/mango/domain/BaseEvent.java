/*
 * Copyright (C) 2013 The Calrissian Authors
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
package org.calrissian.mango.domain;


import static java.lang.System.currentTimeMillis;
import static java.util.UUID.randomUUID;

/**
 * Default implementation of {@link org.calrissian.mango.domain.Event}
 */
public class BaseEvent extends BaseTupleStore implements Event {

    protected final String id;
    protected final long timestamp; // in Millis

    /**
     * New event with random UUID and timestamp defaulted to current time
     */
    public BaseEvent() {
        this(randomUUID().toString());
    }

    /**
     * New event with ID. Timestamp defaults to current time.
     *
     * @param id
     */
    public BaseEvent(String id) {
        this(id, currentTimeMillis());
    }

    /**
     * New store entry with ID and a timestamp
     *
     * @param id
     * @param timestamp
     */
    public BaseEvent(String id, long timestamp) {
        this.id = id;
        this.timestamp = timestamp;
    }

    /**
     * Copy constructor
     */
    public BaseEvent(Event event) {
        this(event.getId(), event.getTimestamp());
        getTuples().addAll(event.getTuples());
    }


    /**
     * {@inheritDoc}
     */
    public String getId() {
        return id;
    }

    /**
     * {@inheritDoc}
     */
    public Long getTimestamp() {
        return timestamp;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        BaseEvent baseEvent = (BaseEvent) o;

        if (timestamp != baseEvent.timestamp) return false;
        if (id != null ? !id.equals(baseEvent.id) : baseEvent.id != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (id != null ? id.hashCode() : 0);
        result = 31 * result + (int) (timestamp ^ (timestamp >>> 32));
        return result;
    }

    @Override
    public String toString() {
        return "BaseEvent{" +
                "id='" + id + '\'' +
                ", timestamp=" + timestamp +
                ", tuples=" + getTuples() +
                '}';
    }
}
