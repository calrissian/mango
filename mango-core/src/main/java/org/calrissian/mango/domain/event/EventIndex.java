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

import org.calrissian.mango.domain.entity.EntityIndex;

public class EventIndex extends EntityIndex {

    private final Long timestamp;

    public EventIndex(String type, String id, Long timestamp) {
        super(type, id);
        this.timestamp = timestamp;
    }

    @Deprecated
    public EventIndex(String id, Long timestamp) {
        this("", id, timestamp);
    }

    public EventIndex(Event event) {
        this(event.getType(), event.getId(), event.getTimestamp());
    }

    public EventIndex(String type, String id) {
        this(type, id, null);
    }

    @Deprecated
    public EventIndex(String id) {
        this("", id, null);
    }

    public Long getTimestamp() {
        return timestamp;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof EventIndex)) return false;
        if (!super.equals(o)) return false;

        EventIndex that = (EventIndex) o;

        if (timestamp != null ? !timestamp.equals(that.timestamp) : that.timestamp != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (timestamp != null ? timestamp.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "EventIndex{" +
                "type='" + getType() + '\'' +
                ", id='" + getId() + '\'' +
                ", timestamp=" + timestamp +
                '}';
    }
}
