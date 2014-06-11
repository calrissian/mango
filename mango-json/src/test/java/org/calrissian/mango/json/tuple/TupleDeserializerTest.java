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
package org.calrissian.mango.json.tuple;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.calrissian.mango.domain.Tuple;
import org.junit.Test;

import java.io.IOException;
import java.util.Map;
import java.util.Set;

import static org.calrissian.mango.types.SimpleTypeEncoders.SIMPLE_TYPES;
import static org.junit.Assert.assertEquals;

public class TupleDeserializerTest {

    private ObjectMapper objectMapper = new ObjectMapper()
            .registerModule(new TupleModule(SIMPLE_TYPES));


    @Test
    public void testBasicSerialization() throws IOException {

        Tuple tuple = objectMapper.readValue("{\"key\":\"key\",\"type\":\"string\",\"value\":\"value\",\"metadata\":[]}", Tuple.class);
        assertEquals("key", tuple.getKey());
        assertEquals("value", tuple.getValue());
        assertEquals(0, tuple.getMetadata().size());
    }

    @Test
    public void testSerialization_withMetadata() throws IOException {
        Tuple tuple = objectMapper.readValue("{\"key\":\"key\",\"type\":\"string\",\"value\":\"value\",\"metadata\":[{\"value\":\"metaVal\",\"type\":\"string\",\"key\":\"metaKey\"}]}", Tuple.class);
        assertEquals("key", tuple.getKey());
        assertEquals("value", tuple.getValue());
        assertEquals(1, tuple.getMetadata().size());

        Set<Map.Entry<String, Object>> entrySet = tuple.getMetadata().entrySet();
        assertEquals("metaKey", entrySet.iterator().next().getKey());
        assertEquals("metaVal", entrySet.iterator().next().getValue());
    }



}

