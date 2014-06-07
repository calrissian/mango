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
package org.calrissian.mango.json.tuple;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.calrissian.mango.domain.Tuple;
import org.calrissian.mango.types.TypeRegistry;
import org.calrissian.mango.types.exception.TypeEncodingException;

import java.io.IOException;
import java.util.Map;
import java.util.Set;

public class TupleSerializer extends JsonSerializer<Tuple> {

    private final TypeRegistry<String> typeContext;

    public TupleSerializer(TypeRegistry<String> typeContext) {
        this.typeContext = typeContext;
    }

    @Override
    public void serialize(Tuple tuple, JsonGenerator jsonGenerator, SerializerProvider serializerProvider)
            throws IOException {
        jsonGenerator.writeStartObject();

        jsonGenerator.writeStringField("key", tuple.getKey());
        try {
            Object value = tuple.getValue();
            if (value != null) {
                String type = typeContext.getAlias(value);
                String val_str = typeContext.encode(value);
                jsonGenerator.writeStringField("type", type);
                jsonGenerator.writeStringField("value", val_str);

                jsonGenerator.writeArrayFieldStart("metadata");

                Set<Map.Entry<String,Object>> entries = tuple.getMetadata().entrySet();
                for(Map.Entry<String,Object> objectEntry : entries) {
                    jsonGenerator.writeStartObject();
                    jsonGenerator.writeObjectField("value", typeContext.encode(objectEntry.getValue()));
                    jsonGenerator.writeObjectField("type", typeContext.getAlias(objectEntry.getValue()));
                    jsonGenerator.writeObjectField("key", objectEntry.getKey());
                    jsonGenerator.writeEndObject();
                }
                jsonGenerator.writeEndArray();

            }
        } catch (TypeEncodingException e) {
            throw new RuntimeException(e);
        }

        jsonGenerator.writeEndObject();
    }
}
