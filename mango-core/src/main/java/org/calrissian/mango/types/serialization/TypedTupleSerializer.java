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
import org.calrissian.mango.types.TypeContext;
import org.calrissian.mango.types.exception.TypeNormalizationException;
import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.JsonSerializer;
import org.codehaus.jackson.map.SerializerProvider;

import java.io.IOException;

/**
 * Date: 9/12/12
 * Time: 2:52 PM
 */
public class TypedTupleSerializer extends JsonSerializer<Tuple> {

    @Override
    public void serialize(Tuple tuple, JsonGenerator jsonGenerator, SerializerProvider serializerProvider)
            throws IOException, JsonProcessingException {
        jsonGenerator.writeStartObject();

        TypeContext instance = TypeContext.getInstance();

        jsonGenerator.writeStringField("key", tuple.getKey());
        String visibility = tuple.getVisibility();
        if (visibility != null)
            jsonGenerator.writeStringField("visibility", visibility);
        try {
            Object value = tuple.getValue();
            if (value != null) {
                String type = instance.getAliasForType(value);
                String val_str = instance.asString(value);
                jsonGenerator.writeStringField("type", type);
                jsonGenerator.writeStringField("value", val_str);
            }
        } catch (TypeNormalizationException e) {
            throw new RuntimeException(e);
        }

        jsonGenerator.writeEndObject();
    }
}
