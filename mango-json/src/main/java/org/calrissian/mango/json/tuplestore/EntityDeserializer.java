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
package org.calrissian.mango.json.tuplestore;


import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import org.calrissian.mango.domain.Tuple;
import org.calrissian.mango.domain.entity.BaseEntity;

import java.io.IOException;
import java.util.List;

/**
 * Date: 9/12/12
 * Time: 2:56 PM
 */
public class EntityDeserializer extends JsonDeserializer<BaseEntity> {

    @Override
    public BaseEntity deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
        JsonNode root = jsonParser.getCodec().readTree(jsonParser);
        ArrayNode tuplesArray = (ArrayNode) root.get("tuples");
        String type = root.get("type").asText();
        String id = root.get("id").asText();

        BaseEntity toReturn =  new BaseEntity(type, id);

        List<Tuple> tuples = jsonParser.getCodec().readValue(jsonParser.getCodec().treeAsTokens(tuplesArray), new TypeReference<List<Tuple>>() {});

        toReturn.putAll(tuples);

        return toReturn;

    }
}
