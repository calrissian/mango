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
package org.calrissian.mango.json.ser;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.calrissian.mango.domain.Tuple;
import org.calrissian.mango.domain.event.BaseEvent;
import org.calrissian.mango.domain.event.Event;
import org.calrissian.mango.json.MangoModule;
import org.junit.Test;

import java.util.Date;

import static org.calrissian.mango.types.SimpleTypeEncoders.SIMPLE_TYPES;
import static org.junit.Assert.assertEquals;

public class EventSerializerTest {

    private ObjectMapper objectMapper = new ObjectMapper()
            .registerModule(new MangoModule(SIMPLE_TYPES));

    @Test
    public void testSerializes() throws JsonProcessingException {

        Event event = new BaseEvent("", "id", new Date(0).getTime());
        event.put(new Tuple("key", "value"));
        event.put(new Tuple("key1", "valu1"));

        String serialized = objectMapper.writeValueAsString(event);

        assertEquals(serialized, "{\"timestamp\":0,\"type\":\"\",\"id\":\"id\",\"tuples\":{\"key1\":[{\"key\":\"key1\",\"type\":\"string\",\"value\":\"valu1\",\"metadata\":[]}],\"key\":[{\"key\":\"key\",\"type\":\"string\",\"value\":\"value\",\"metadata\":[]}]}}");
    }



}
