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


import com.fasterxml.jackson.databind.ObjectMapper;
import org.calrissian.mango.domain.Tuple;
import org.calrissian.mango.domain.entity.BaseEntity;
import org.calrissian.mango.domain.entity.Entity;
import org.calrissian.mango.domain.event.BaseEvent;
import org.calrissian.mango.domain.event.Event;
import org.calrissian.mango.json.MangoModule;
import org.junit.Test;

import java.util.HashSet;

import static org.calrissian.mango.types.SimpleTypeEncoders.SIMPLE_TYPES;
import static org.junit.Assert.assertEquals;

public class EventDeserializerTest {

    private ObjectMapper objectMapper = new ObjectMapper()
            .registerModule(new MangoModule(SIMPLE_TYPES));

    @Test
    public void testBasicDeserialization() throws Exception {

        Event event = new BaseEvent();
        event.put(new Tuple("key", "value"));

        String json = objectMapper.writeValueAsString(event);

        Event actualEntity = objectMapper.readValue(json, Event.class);

        assertEquals(actualEntity.getId(), event.getId());
        assertEquals(actualEntity.getTimestamp(), event.getTimestamp());
        assertEquals(new HashSet<Tuple>(actualEntity.getTuples()), new HashSet<Tuple>(event.getTuples()));
    }


}
