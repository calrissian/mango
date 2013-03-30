package org.calrissian.mango.types.serialization;

import org.calrissian.commons.domain.Tuple;
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
public class TupleSerializer extends JsonSerializer<Tuple> {
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
