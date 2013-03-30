package org.calrissian.criteria.serialization;

import org.calrissian.criteria.domain.*;
import org.calrissian.mango.types.TypeContext;
import org.calrissian.mango.types.exception.TypeNormalizationException;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.DeserializationContext;
import org.codehaus.jackson.map.JsonDeserializer;
import org.codehaus.jackson.node.ArrayNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Iterator;

/**
 * Date: 11/15/12
 * Time: 11:34 AM
 */
public class NodeDeserializer extends JsonDeserializer<Node> {
    private static final Logger logger = LoggerFactory.getLogger(NodeDeserializer.class);
    private TypeContext typeContext = TypeContext.getInstance();

    /**
     * {"or":{"children":[{"and":{"children":[{"eq":{"key":"k1","type":"string","value":"v1"}},{"neq":{"key":"k2","type":"ipv4","value":"1.2.3.4"}}]}},{"and":{"children":[{"eq":{"key":"k3","type":"integer","value":"1234"}}]}}]}}
     */
    @Override
    public Node deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
        JsonNode root = jsonParser.getCodec().readTree(jsonParser);
        Iterator<String> fieldNames = root.getFieldNames();
        if (fieldNames.hasNext()) {
            String fieldKey = fieldNames.next();
            JsonNode fieldJson = root.get(fieldKey);
            return parseField(fieldKey, fieldJson); //only expecting one root node
        }
        return null;
    }

    protected Node parseField(String fieldKey, JsonNode fieldJson) {
        try {
            if ("and".equals(fieldKey)) {
                AndNode andNode = new AndNode();
                JsonNode children = fieldJson.get("children");
                if (children instanceof ArrayNode) {
                    ArrayNode childrenArray = (ArrayNode) children;
                    Iterator<JsonNode> elements = childrenArray.getElements();
                    while (elements.hasNext()) {
                        JsonNode entry = elements.next();
                        Iterator<String> fieldNames = entry.getFieldNames();
                        while (fieldNames.hasNext()) {
                            String key = fieldNames.next();
                            andNode.addChild(parseField(key, entry.get(key)));
                        }
                    }
                }
                return andNode;
            } else if ("or".equals(fieldKey)) {
                OrNode orNode = new OrNode();
                JsonNode children = fieldJson.get("children");
                if (children instanceof ArrayNode) {
                    ArrayNode childrenArray = (ArrayNode) children;
                    Iterator<JsonNode> elements = childrenArray.getElements();
                    while (elements.hasNext()) {
                        JsonNode entry = elements.next();
                        Iterator<String> fieldNames = entry.getFieldNames();
                        while (fieldNames.hasNext()) {
                            String key = fieldNames.next();
                            orNode.addChild(parseField(key, entry.get(key)));
                        }
                    }
                }
                return orNode;
            } else if ("eq".equals(fieldKey)) {
                String key = fieldJson.get("key").getValueAsText();
                String type = fieldJson.get("type").getValueAsText();
                String val_str = fieldJson.get("value").getValueAsText();

                Object obj = typeContext.fromString(val_str, type);
                return new EqualsLeaf(key, obj, null);
            } else if ("neq".equals(fieldKey)) {
                String key = fieldJson.get("key").getValueAsText();
                String type = fieldJson.get("type").getValueAsText();
                String val_str = fieldJson.get("value").getValueAsText();

                Object obj = typeContext.fromString(val_str, type);
                return new NotEqualsLeaf(key, obj, null);
            } else if ("range".equals(fieldKey)) {
                String key = fieldJson.get("key").getValueAsText();
                String type = fieldJson.get("type").getValueAsText();
                String start_str = fieldJson.get("start").getValueAsText();
                String end_str = fieldJson.get("end").getValueAsText();

                Object start = typeContext.fromString(start_str, type);
                Object end = typeContext.fromString(end_str, type);
                return new RangeLeaf(key, start, end, null);
            } else {
                throw new IllegalArgumentException("Unsupported field: " + fieldKey);
            }
        } catch (TypeNormalizationException e) {
            logger.warn("Type Normalization Exception for field key: " + fieldKey + " exception message: " + e.getMessage());
        }
        return null;

    }
}
