/*
 * Copyright (C) 2014 The Calrissian Authors
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
package org.calrissian.mango.json.deser;


import com.fasterxml.jackson.databind.JsonNode;
import org.calrissian.mango.domain.event.BaseEvent;

public class EventDeserializer extends BaseAttributeStoreDeserializer<BaseEvent> {

    @Override
    public BaseEvent deserialize(JsonNode root) {
        String type = "";

        //TODO Next major release (2.x) remove this check as this should now be required.
        if (root.has("type"))
            type = root.get("type").asText();

        String id = root.get("id").asText();
        long timestamp = root.get("timestamp").asLong();


        return new BaseEvent(type, id, timestamp);
    }

}
