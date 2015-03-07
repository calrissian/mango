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

import com.google.common.base.Preconditions;
import org.calrissian.mango.domain.BaseAttributeStoreBuilder;

public class EventBuilder extends BaseAttributeStoreBuilder<Event, EventBuilder> {

    protected String type;
    protected String id;


    protected long timestamp;

    public EventBuilder(String type, String id, long timestamp) {
        super();
        Preconditions.checkNotNull(type);
        Preconditions.checkNotNull(id);
        this.type = type;
        this.id = id;
        this.timestamp = timestamp;
    }

    @Override
    public Event build() {
        return new BaseEvent(type, id, timestamp, attributes);
    }
}
