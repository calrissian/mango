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


import com.google.common.base.Predicate;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import org.calrissian.mango.domain.Attribute;
import org.calrissian.mango.domain.entity.BaseEntity;

import java.util.Collection;
import java.util.Set;

import static com.google.common.collect.Collections2.filter;

/**
 * Default implementation of {@link Event}
 */
public class BaseEvent extends BaseEntity implements Event {

    private static final String TIMESTAMP_FIELD = "_TIMESTAMP";

    BaseEvent(String type, String id, long timestamp, Multimap<String, Attribute> attributes) {
        super(type, id, attributes);
        addTimestamp(timestamp);
    }

    /**
     * Copy constructor
     */
    public BaseEvent(Event event) {
        this(event.getType(), event.getId(), event.getTimestamp(), ArrayListMultimap.<String,Attribute>create());
        putAll(event.getAttributes());
    }

    /**
     * {@inheritDoc}
     */
    public long getTimestamp() {
        return super.get(TIMESTAMP_FIELD, Long.class).getValue();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BaseEvent)) return false;
        if (!super.equals(o)) return false;

        BaseEvent baseEvent = (BaseEvent) o;

        if (getTimestamp() != baseEvent.getTimestamp()) return false;

        return true;
    }

    @Override
    public Collection<Attribute> getAttributes() {
        return filter(super.getAttributes(), filterTimestamp) ;
    }

    @Override
    public Set<String> keys() {
        Set<String> keys = super.keys();
        keys.remove(TIMESTAMP_FIELD);
        return keys;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (int) (getTimestamp() ^ (getTimestamp() >>> 32));
        return result;
    }

    @Override
    public String toString() {
        return "BaseEvent{" +
                "type='" + getType() + '\'' +
                ", id='" + getId() + '\'' +
                ", timestamp=" + getTimestamp() +
                ", attributes=" + getAttributes() +
                '}';
    }

    private Predicate<Attribute> filterTimestamp = new Predicate<Attribute>() {

        @Override
        public boolean apply(Attribute attribute) {
        return !attribute.getKey().equals(TIMESTAMP_FIELD);
        }
    };

    private void addTimestamp(long timestamp) {
        attributes.put(TIMESTAMP_FIELD, new Attribute(TIMESTAMP_FIELD, timestamp));
    }

}
