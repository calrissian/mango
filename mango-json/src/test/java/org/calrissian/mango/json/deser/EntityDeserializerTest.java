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
import org.calrissian.mango.domain.entity.Entity;
import org.calrissian.mango.json.MangoModule;
import org.junit.Test;

import java.util.HashSet;

import static org.calrissian.mango.domain.entity.EntityBuilder.create;
import static org.calrissian.mango.types.SimpleTypeEncoders.SIMPLE_TYPES;
import static org.junit.Assert.assertEquals;

public class EntityDeserializerTest {

    private ObjectMapper objectMapper = new ObjectMapper()
            .registerModule(new MangoModule(SIMPLE_TYPES));

    @Test
    public void testBasicDeserialization() throws Exception {

        Entity entity = create("type", "id").attr("key", "value").build();

        String json = objectMapper.writeValueAsString(entity);

        Entity actualEntity = objectMapper.readValue(json, Entity.class);

        assertEquals(actualEntity.getType(), entity.getType());
        assertEquals(actualEntity.getId(), entity.getId());
        assertEquals(new HashSet<>(actualEntity.getAttributes()), new HashSet<>(entity.getAttributes()));
    }


}
