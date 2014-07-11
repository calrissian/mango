package org.calrissian.mango.json.ser;


import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.calrissian.mango.domain.TupleStore;

import java.io.IOException;

public abstract class BaseTupleStoreSerializer<T extends TupleStore> extends JsonSerializer<T> {


    @Override
    public void serialize(T t, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException, JsonProcessingException {

        jsonGenerator.writeStartObject();

        writeUniqueFields(t, jsonGenerator);

        jsonGenerator.writeObjectFieldStart("tuples");

        for(String key : t.keys())
            jsonGenerator.writeObjectField(key, t.getAll(key));

        jsonGenerator.writeEndObject();

    }

    protected abstract void writeUniqueFields(T t, JsonGenerator generator) throws IOException;
}
