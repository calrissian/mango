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
package org.calrissian.mango.criteria.serialization;

import org.calrissian.mango.criteria.domain.*;
import org.calrissian.mango.types.TypeContext;
import org.calrissian.mango.types.exception.TypeNormalizationException;
import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.map.JsonSerializer;
import org.codehaus.jackson.map.SerializerProvider;

import java.io.IOException;

/**
 * Date: 11/15/12
 * Time: 10:01 AM
 */
public class NodeSerializer extends JsonSerializer<Node> {

    private final TypeContext typeContext;

    public NodeSerializer(TypeContext typeContext) {
        this.typeContext = typeContext;
    }

    @Override
    public void serialize(Node node, JsonGenerator jsonGenerator, SerializerProvider serializerProvider)
            throws IOException {

        jsonGenerator.writeStartObject();
        try {
            if (node instanceof ParentNode) {
                serialize((ParentNode) node, jsonGenerator, serializerProvider);
            } else if (node instanceof Leaf) {
                serialize((Leaf) node, jsonGenerator, serializerProvider);
            } else {
                throw new IllegalArgumentException("Unsupported node: " + node);
            }
        } catch (TypeNormalizationException e) {
            throw new IOException(e);
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
        for(Node child: node.children()) {
            serialize(child, jsonGenerator, serializerProvider);
        }
        jsonGenerator.writeEndArray();
        jsonGenerator.writeEndObject();

    }

    public void serialize(Leaf node, JsonGenerator jsonGenerator, SerializerProvider serializerProvider)
            throws IOException, TypeNormalizationException {

        if (node instanceof EqualsLeaf) {
            //eq
            jsonGenerator.writeObjectFieldStart("eq");
            EqualsLeaf equalsLeaf = (EqualsLeaf) node;
            jsonGenerator.writeStringField("key", equalsLeaf.getKey());

            Object value = equalsLeaf.getValue();
            String type = typeContext.getAliasForType(value);
            String val_str = typeContext.asString(value);
            jsonGenerator.writeStringField("type", type);
            jsonGenerator.writeStringField("value", val_str);

            jsonGenerator.writeEndObject();
        } else if (node instanceof NotEqualsLeaf) {
            //neq
            NotEqualsLeaf leaf = (NotEqualsLeaf) node;
            jsonGenerator.writeObjectFieldStart("neq");
            jsonGenerator.writeStringField("key", leaf.getKey());

            Object value = leaf.getValue();
            String type = typeContext.getAliasForType(value);
            String val_str = typeContext.asString(value);
            jsonGenerator.writeStringField("type", type);
            jsonGenerator.writeStringField("value", val_str);

            jsonGenerator.writeEndObject();
        } else if (node instanceof RangeLeaf) {
            //range
            RangeLeaf leaf = (RangeLeaf) node;
            jsonGenerator.writeObjectFieldStart("range");
            jsonGenerator.writeStringField("key", leaf.getKey());

            Object start = leaf.getStart();
            String type = typeContext.getAliasForType(start);
            String val_str = typeContext.asString(start);
            jsonGenerator.writeStringField("type", type);
            jsonGenerator.writeStringField("start", val_str);

            Object end = leaf.getEnd();
            val_str = typeContext.asString(end);
            jsonGenerator.writeStringField("end", val_str);

            jsonGenerator.writeEndObject();
        } else throw new IllegalArgumentException("Unsupported leaf: " + node);


    }
}
