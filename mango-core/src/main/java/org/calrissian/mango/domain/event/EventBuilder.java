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

import org.calrissian.mango.domain.AbstractAttributeStoreBuilder;

import static com.google.common.base.Preconditions.checkNotNull;

public class EventBuilder extends AbstractAttributeStoreBuilder<Event, EventBuilder> {

    private final EventIdentifier identifier;

    public static EventBuilder create(EventIdentifier identifier) {
        checkNotNull(identifier);
        return new EventBuilder(identifier);
    }

    public static EventBuilder create(String type, String id, long timestamp) {
        return create(new EventIdentifier(type, id, timestamp));
    }

    public static EventBuilder create(Event event) {
        return create(event.getIdentifier())
                .attrs(event.getAttributes());
    }

    protected EventBuilder(EventIdentifier identifier) {
        super();
        this.identifier = identifier;
    }

    @Override
    public Event build() {
        return new BaseEvent(identifier, attributes);
    }
}
