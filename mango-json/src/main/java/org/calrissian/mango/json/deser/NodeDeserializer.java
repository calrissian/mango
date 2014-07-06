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
package org.calrissian.mango.json.deser;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import org.calrissian.mango.criteria.domain.*;
import org.calrissian.mango.types.TypeRegistry;

import java.io.IOException;
import java.util.Iterator;

public class NodeDeserializer extends JsonDeserializer<Node> {

    private final TypeRegistry<String> typeRegistry;

    public NodeDeserializer(TypeRegistry<String> typeRegistry) {
        this.typeRegistry = typeRegistry;
    }

    /**
     * {"or":{"children":[{"and":{"children":[{"eq":{"key":"k1","type":"string","value":"v1"}},{"neq":{"key":"k2","type":"ipv4","value":"1.2.3.4"}}]}},{"and":{"children":[{"eq":{"key":"k3","type":"integer","value":"1234"}}]}}]}}
     */
    @Override
    public Node deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
        JsonNode root = jsonParser.getCodec().readTree(jsonParser);
        Iterator<String> fieldNames = root.fieldNames();
        if (fieldNames.hasNext()) {
            String fieldKey = fieldNames.next();
            JsonNode fieldJson = root.get(fieldKey);
            return parseField(fieldKey, fieldJson); //only expecting one root node
        }
        return null;
    }

    protected Node parseField(String fieldKey, JsonNode fieldJson) throws IOException {
        if ("and".equals(fieldKey)) {
            AndNode andNode = new AndNode();
            JsonNode children = fieldJson.get("children");
            if (children instanceof ArrayNode) {
                ArrayNode childrenArray = (ArrayNode) children;
                Iterator<JsonNode> elements = childrenArray.elements();
                while (elements.hasNext()) {
                    JsonNode entry = elements.next();
                    Iterator<String> fieldNames = entry.fieldNames();
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
                Iterator<JsonNode> elements = childrenArray.elements();
                while (elements.hasNext()) {
                    JsonNode entry = elements.next();
                    Iterator<String> fieldNames = entry.fieldNames();
                    while (fieldNames.hasNext()) {
                        String key = fieldNames.next();
                        orNode.addChild(parseField(key, entry.get(key)));
                    }
                }
            }
            return orNode;
        } else if ("eq".equals(fieldKey)) {
            String key = fieldJson.get("key").asText();
            String type = fieldJson.get("type").asText();
            String val_str = fieldJson.get("value").asText();

            Object obj = this.typeRegistry.decode(type, val_str);
            return new EqualsLeaf(key, obj, null);
        } else if ("neq".equals(fieldKey)) {
            String key = fieldJson.get("key").asText();
            String type = fieldJson.get("type").asText();
            String val_str = fieldJson.get("value").asText();

            Object obj = this.typeRegistry.decode(type, val_str);
            return new NotEqualsLeaf(key, obj, null);
        } else if ("range".equals(fieldKey)) {
            String key = fieldJson.get("key").asText();
            String type = fieldJson.get("type").asText();
            String start_str = fieldJson.get("start").asText();
            String end_str = fieldJson.get("end").asText();

            Object start = this.typeRegistry.decode(type, start_str);
            Object end = this.typeRegistry.decode(type, end_str);
            return new RangeLeaf(key, start, end, null);
        } else {
            throw new IllegalArgumentException("Unsupported field: " + fieldKey);
        }

    }
}
