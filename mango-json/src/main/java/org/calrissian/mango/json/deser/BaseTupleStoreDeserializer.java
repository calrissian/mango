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

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.calrissian.mango.domain.Attribute;
import org.calrissian.mango.domain.AttributeStore;

import java.io.IOException;
import java.util.Collection;
import java.util.Map;

import static com.google.common.collect.Iterables.concat;

public abstract class BaseTupleStoreDeserializer<T extends AttributeStore> extends JsonDeserializer<T> {

    private static final TypeReference TR = new TypeReference<Map<String, Collection<Attribute>>>(){};
    @Override
    public T deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
        JsonNode root = jsonParser.getCodec().readTree(jsonParser);
        T tupleStore = deserialize(root);

        ObjectNode tuplesObject = (ObjectNode) root.get("tuples");

        Map<String, Collection<Attribute>> tuples =
                jsonParser.getCodec().readValue(jsonParser.getCodec().treeAsTokens(tuplesObject), TR);

        tupleStore.putAll(concat(tuples.values()));

        return tupleStore;


    }

    public abstract T deserialize(JsonNode root);
}
