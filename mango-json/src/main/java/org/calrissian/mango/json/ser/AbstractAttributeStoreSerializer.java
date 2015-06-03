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


import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.calrissian.mango.domain.AttributeStore;

import java.io.IOException;

abstract class AbstractAttributeStoreSerializer<T extends AttributeStore> extends JsonSerializer<T> {


    @Override
    public void serialize(T t, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {

        jsonGenerator.writeStartObject();

        writeUniqueFields(t, jsonGenerator);

        jsonGenerator.writeObjectFieldStart("attributes");

        for(String key : t.keys())
            jsonGenerator.writeObjectField(key, t.getAttributes(key));

        jsonGenerator.writeEndObject();

    }

    protected abstract void writeUniqueFields(T t, JsonGenerator generator) throws IOException;
}
