/*
 * Copyright (C) 2019 The Calrissian Authors
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
import org.calrissian.mango.domain.Attribute;
import org.calrissian.mango.json.MangoModule;
import org.junit.Test;

import java.io.IOException;
import java.util.Map;
import java.util.Set;

import static org.calrissian.mango.types.SimpleTypeEncoders.SIMPLE_TYPES;
import static org.junit.Assert.assertEquals;

public class AttributeDeserializerTest {

    private ObjectMapper objectMapper = new ObjectMapper()
            .registerModule(new MangoModule(SIMPLE_TYPES));


    @Test
    public void testBasicSerialization() throws IOException {

        Attribute attribute = objectMapper.readValue("{\"key\":\"key\",\"type\":\"string\",\"value\":\"value\",\"metadata\":[]}", Attribute.class);
        assertEquals("key", attribute.getKey());
        assertEquals("value", attribute.getValue());
        assertEquals(0, attribute.getMetadata().size());
    }

    @Test
    public void testSerialization_withMetadata() throws IOException {
        Attribute attribute = objectMapper.readValue("{\"key\":\"key\",\"type\":\"string\",\"value\":\"value\",\"metadata\":[{\"value\":\"metaVal\",\"key\":\"metaKey\"}]}", Attribute.class);
        assertEquals("key", attribute.getKey());
        assertEquals("value", attribute.getValue());
        assertEquals(1, attribute.getMetadata().size());

        Set<Map.Entry<String, String>> entrySet = attribute.getMetadata().entrySet();
        assertEquals("metaKey", entrySet.iterator().next().getKey());
        assertEquals("metaVal", entrySet.iterator().next().getValue());
    }
}

