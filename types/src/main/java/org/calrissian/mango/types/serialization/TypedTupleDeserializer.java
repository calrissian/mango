package org.calrissian.mango.types.serialization;

import org.calrissian.commons.domain.Tuple;
import org.calrissian.mango.types.TypeContext;
import org.calrissian.mango.types.exception.TypeNormalizationException;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.map.DeserializationContext;
import org.codehaus.jackson.map.JsonDeserializer;

import java.io.IOException;

/**
 * Date: 9/12/12
 * Time: 2:56 PM
 */
public class TypedTupleDeserializer extends JsonDeserializer<Tuple> {
    @Override
    public Tuple deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
        JsonNode root = jsonParser.getCodec().readTree(jsonParser);
        String key = root.get("key").getValueAsText();
        JsonNode vis_json = root.get("visibility");
        String visibility = vis_json != null ? vis_json.getValueAsText() : null;
        Object value = null;
        JsonNode type_json = root.get("type");
        if (type_json != null) {
            String type = type_json.getValueAsText();
            String val_str = root.get("value").getValueAsText();
            try {
                value = TypeContext.getInstance().fromString(val_str, type);
            } catch (TypeNormalizationException e) {
                throw new RuntimeException(e);
            }
        }
        return new Tuple(key, value, visibility);
    }
}
