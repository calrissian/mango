/*
 * Copyright (C) 2016 The Calrissian Authors
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
package org.calrissian.mango.json.ser;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.calrissian.mango.domain.event.Event;
import org.calrissian.mango.json.MangoModule;
import org.junit.Test;

import static org.calrissian.mango.domain.event.EventBuilder.create;
import static org.calrissian.mango.types.SimpleTypeEncoders.SIMPLE_TYPES;
import static org.junit.Assert.assertEquals;

public class EventSerializerTest {

    private ObjectMapper objectMapper = new ObjectMapper()
            .registerModule(new MangoModule(SIMPLE_TYPES));

    @Test
    public void testSerializes() throws JsonProcessingException {

        Event event = create("", "id", 0)
                .attr("key", "value")
                .attr("key1", "valu1")
                .build();

        String serialized = objectMapper.writeValueAsString(event);

        assertEquals(serialized, "{\"type\":\"\",\"id\":\"id\",\"timestamp\":0,\"attributes\":{\"key1\":[{\"key\":\"key1\",\"type\":\"string\",\"value\":\"valu1\",\"metadata\":[]}],\"key\":[{\"key\":\"key\",\"type\":\"string\",\"value\":\"value\",\"metadata\":[]}]}}");
    }
}
