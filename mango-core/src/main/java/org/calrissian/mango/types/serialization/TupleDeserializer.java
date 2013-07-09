/*
 * Copyright (C) 2013 The Calrissian Authors
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
package org.calrissian.mango.types.serialization;


import org.calrissian.mango.domain.Tuple;
import org.calrissian.mango.types.TypeRegistry;
import org.calrissian.mango.types.exception.TypeDecodingException;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.map.DeserializationContext;
import org.codehaus.jackson.map.JsonDeserializer;

import java.io.IOException;

/**
 * Date: 9/12/12
 * Time: 2:56 PM
 */
public class TupleDeserializer extends JsonDeserializer<Tuple> {

    private final TypeRegistry<String> typeRegistry;

    public TupleDeserializer(TypeRegistry<String> typeRegistry) {
        this.typeRegistry = typeRegistry;
    }

    @Override
    public Tuple deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
        JsonNode root = jsonParser.getCodec().readTree(jsonParser);
        String key = root.get("key").asText();
        JsonNode vis_json = root.get("visibility");
        String visibility = vis_json != null ? vis_json.asText() : null;
        Object value = null;
        JsonNode type_json = root.get("type");
        if (type_json != null) {
            String type = type_json.asText();
            String val_str = root.get("value").asText();
            try {
                value = typeRegistry.decode(type, val_str);
            } catch (TypeDecodingException e) {
                throw new RuntimeException(e);
            }
        }
        return new Tuple(key, value, visibility);
    }
}
