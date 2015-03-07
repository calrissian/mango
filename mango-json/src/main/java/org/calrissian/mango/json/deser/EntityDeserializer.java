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
import org.calrissian.mango.domain.entity.BaseEntity;
import org.calrissian.mango.domain.entity.EntityBuilder;

public class EntityDeserializer extends BaseAttributeStoreDeserializer<BaseEntity, EntityBuilder> {

    @Override
    public EntityBuilder deserialize(JsonNode root) {
        String type = root.get("type").asText();
        String id = root.get("id").asText();

        return new EntityBuilder(type, id);
    }

    @Override
    public BaseEntity build(EntityBuilder entityBuilder) {
        return (BaseEntity)entityBuilder.build();
    }
}
