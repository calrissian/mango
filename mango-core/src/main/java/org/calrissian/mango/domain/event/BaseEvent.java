/*
 * Copyright (C) 2017 The Calrissian Authors
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

import org.calrissian.mango.domain.Attribute;
import org.calrissian.mango.domain.entity.BaseEntity;

/**
 * Default implementation of {@link Event}
 */
public class BaseEvent extends BaseEntity implements Event {

    private static final long serialVersionUID = 1L;

    public BaseEvent(EventIdentifier identifier, Iterable<? extends Attribute> attributes) {
        super(identifier, attributes);
    }

    /**
     * Copy constructor
     */
    public BaseEvent(Event event) {
        this(event.getIdentifier(), event.getAttributes());
    }

    /**
     * {@inheritDoc}
     */
    public long getTimestamp() {
        return ((EventIdentifier)super.getIdentifier()).getTimestamp();
    }

    @Override
    public EventIdentifier getIdentifier() {
        return (EventIdentifier) super.getIdentifier();
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
}
