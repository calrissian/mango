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

import org.calrissian.mango.domain.Identifiable;
import org.calrissian.mango.domain.Temporal;
import org.calrissian.mango.domain.entity.EntityIdentifier;

public class EventIdentifier extends EntityIdentifier implements Identifiable, Temporal {

    private final long timestamp;

    public EventIdentifier(String type, String id, long timestamp) {
        super(type, id);
        this.timestamp = timestamp;
    }

    @Override
    public long getTimestamp() {
        return timestamp;
    }

    @Override
    public String toString() {
        return "EventIndex{" +
                "type='" + getType() + '\'' +
                ", id='" + getId() + '\'' +
                ", timestamp=" + getTimestamp() +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        EventIdentifier that = (EventIdentifier) o;

        return timestamp == that.timestamp;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (int) (timestamp ^ (timestamp >>> 32));
        return result;
    }
}
