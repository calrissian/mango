/*
 * Copyright (C) 2019 The Calrissian Authors
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
import org.calrissian.mango.criteria.domain.*;
import org.calrissian.mango.types.TypeRegistry;

import java.io.IOException;

public class NodeSerializer extends JsonSerializer<Node> {

    private final TypeRegistry<String> typeRegistry;

    public NodeSerializer(TypeRegistry<String> typeRegistry) {
        this.typeRegistry = typeRegistry;
    }

    @Override
    public void serialize(Node node, JsonGenerator jsonGenerator, SerializerProvider serializerProvider)
            throws IOException {

        jsonGenerator.writeStartObject();
        if (node instanceof ParentNode) {
            serialize((ParentNode) node, jsonGenerator, serializerProvider);
        } else if (node instanceof Leaf) {
            serialize((Leaf) node, jsonGenerator, serializerProvider);
        } else {
            throw new IllegalArgumentException("Unsupported node: " + node);
        }

        jsonGenerator.writeEndObject();
    }

    public void serialize(ParentNode node, JsonGenerator jsonGenerator, SerializerProvider serializerProvider)
            throws IOException {

        if (node instanceof AndNode) {
            //and
            jsonGenerator.writeObjectFieldStart("and");

        } else if (node instanceof OrNode) {
            //or
            jsonGenerator.writeObjectFieldStart("or");
        } else throw new IllegalArgumentException("Unsupported parent: " + node);
        jsonGenerator.writeArrayFieldStart("children");
        for (Node child : node.children()) {
            serialize(child, jsonGenerator, serializerProvider);
        }
        jsonGenerator.writeEndArray();
        jsonGenerator.writeEndObject();

    }

    public void serialize(Leaf node, JsonGenerator jsonGenerator, SerializerProvider serializerProvider)
            throws IOException {

        if (node instanceof EqualsLeaf) {
            //eq
            jsonGenerator.writeObjectFieldStart("eq");
            EqualsLeaf equalsLeaf = (EqualsLeaf) node;
            jsonGenerator.writeStringField("term", equalsLeaf.getTerm());

            Object value = equalsLeaf.getValue();
            String type = typeRegistry.getAlias(value);
            String val_str = typeRegistry.encode(value);
            jsonGenerator.writeStringField("type", type);
            jsonGenerator.writeStringField("value", val_str);

            jsonGenerator.writeEndObject();
        } else if (node instanceof NotEqualsLeaf) {
            //neq
            NotEqualsLeaf leaf = (NotEqualsLeaf) node;
            jsonGenerator.writeObjectFieldStart("neq");
            jsonGenerator.writeStringField("term", leaf.getTerm());

            Object value = leaf.getValue();
            String type = typeRegistry.getAlias(value);
            String val_str = typeRegistry.encode(value);
            jsonGenerator.writeStringField("type", type);
            jsonGenerator.writeStringField("value", val_str);

            jsonGenerator.writeEndObject();
        } else if (node instanceof RangeLeaf) {
            //range
            RangeLeaf leaf = (RangeLeaf) node;
            jsonGenerator.writeObjectFieldStart("range");
            jsonGenerator.writeStringField("term", leaf.getTerm());

            Object start = leaf.getStart();
            String type = typeRegistry.getAlias(start);
            String val_str = typeRegistry.encode(start);
            jsonGenerator.writeStringField("type", type);
            jsonGenerator.writeStringField("start", val_str);

            Object end = leaf.getEnd();
            val_str = typeRegistry.encode(end);
            jsonGenerator.writeStringField("end", val_str);

            jsonGenerator.writeEndObject();
        } else throw new IllegalArgumentException("Unsupported leaf: " + node);


    }
}
