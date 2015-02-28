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
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import org.calrissian.mango.domain.Tuple;
import org.calrissian.mango.types.TypeRegistry;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class TupleDeserializer extends JsonDeserializer<Tuple> {

    private final TypeRegistry<String> typeRegistry;

    public TupleDeserializer(TypeRegistry<String> typeRegistry) {
        this.typeRegistry = typeRegistry;
    }

    @Override
    public Tuple deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
        JsonNode root = jsonParser.getCodec().readTree(jsonParser);
        String key = root.get("key").asText();
        Object value = null;
        JsonNode type_json = root.get("type");
        if (type_json != null) {
            String type = type_json.asText();
            String val_str = root.get("value").asText();

            value = typeRegistry.decode(type, val_str);
        }

        Map<String, String> metadata = new HashMap<>();
        JsonNode metadataArray = root.get("metadata");
        if(metadataArray != null) {
            for (JsonNode metadataItem : metadataArray) {
                String metaKey = metadataItem.get("key").asText();
                String normalized = metadataItem.get("value").asText();

                metadata.put(metaKey, normalized);
            }
        }

        return new Tuple(key, value, metadata);

    }
}
