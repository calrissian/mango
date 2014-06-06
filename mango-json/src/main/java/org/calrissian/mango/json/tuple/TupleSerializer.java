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

import java.io.IOException;

/**
 * Date: 9/12/12
 * Time: 2:52 PM
 */
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
        String visibility = tuple.getVisibility();
        if (visibility != null)
            jsonGenerator.writeStringField("visibility", visibility);

        Object value = tuple.getValue();
        if (value != null) {
            String type = typeContext.getAlias(value);
            String val_str = typeContext.encode(value);
            jsonGenerator.writeStringField("type", type);
            jsonGenerator.writeStringField("value", val_str);
        }

        jsonGenerator.writeEndObject();
    }
}
