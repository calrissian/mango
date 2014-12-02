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


import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ImmutableMap;
import org.calrissian.mango.domain.Attribute;
import org.calrissian.mango.json.MangoModule;
import org.junit.Test;

import static org.calrissian.mango.types.SimpleTypeEncoders.SIMPLE_TYPES;
import static org.junit.Assert.assertEquals;

public class AttributeSerializerTest {

    private ObjectMapper objectMapper = new ObjectMapper()
            .registerModule(new MangoModule(SIMPLE_TYPES));

    @Test
    public void testBasicSerialization() throws Exception {
        Attribute keyValue = new Attribute("key", "value");
        String json = objectMapper.writeValueAsString(keyValue);
        assertEquals(json, "{\"key\":\"key\",\"type\":\"string\",\"value\":\"value\",\"metadata\":[]}");
    }

    @Test
    public void testSerialization_withMetadata() throws Exception {
        Attribute keyValue = new Attribute("key", "value", ImmutableMap.of("metaKey", "metaVal"));
        String json = objectMapper.writeValueAsString(keyValue);
        assertEquals(json, "{\"key\":\"key\",\"type\":\"string\",\"value\":\"value\",\"metadata\":[{\"value\":\"metaVal\",\"type\":\"string\",\"key\":\"metaKey\"}]}");
    }

}
