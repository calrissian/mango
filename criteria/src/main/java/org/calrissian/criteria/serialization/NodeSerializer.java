package org.calrissian.criteria.serialization;

import org.calrissian.criteria.domain.*;
import org.calrissian.mango.types.TypeContext;
import org.calrissian.mango.types.exception.TypeNormalizationException;
import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.JsonSerializer;
import org.codehaus.jackson.map.SerializerProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * Date: 11/15/12
 * Time: 10:01 AM
 */
public class NodeSerializer extends JsonSerializer<Node> {
    private static final Logger logger = LoggerFactory.getLogger(NodeSerializer.class);

    private TypeContext instance = TypeContext.getInstance();

    @Override
    public void serialize(Node node, JsonGenerator jsonGenerator, SerializerProvider serializerProvider)
            throws IOException, JsonProcessingException {

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
            throws IOException, JsonProcessingException {

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
            throws IOException, JsonProcessingException, TypeNormalizationException {

        if (node instanceof EqualsLeaf) {
            //eq
            jsonGenerator.writeObjectFieldStart("eq");
            EqualsLeaf equalsLeaf = (EqualsLeaf) node;
            jsonGenerator.writeStringField("key", equalsLeaf.getKey());

            Object value = equalsLeaf.getValue();
            String type = instance.getAliasForType(value);
            String val_str = instance.asString(value);
            jsonGenerator.writeStringField("type", type);
            jsonGenerator.writeStringField("value", val_str);

            jsonGenerator.writeEndObject();
        } else if (node instanceof NotEqualsLeaf) {
            //neq
            NotEqualsLeaf leaf = (NotEqualsLeaf) node;
            jsonGenerator.writeObjectFieldStart("neq");
            jsonGenerator.writeStringField("key", leaf.getKey());

            Object value = leaf.getValue();
            String type = instance.getAliasForType(value);
            String val_str = instance.asString(value);
            jsonGenerator.writeStringField("type", type);
            jsonGenerator.writeStringField("value", val_str);

            jsonGenerator.writeEndObject();
        } else if (node instanceof RangeLeaf) {
            //range
            RangeLeaf leaf = (RangeLeaf) node;
            jsonGenerator.writeObjectFieldStart("range");
            jsonGenerator.writeStringField("key", leaf.getKey());

            Object start = leaf.getStart();
            String type = instance.getAliasForType(start);
            String val_str = instance.asString(start);
            jsonGenerator.writeStringField("type", type);
            jsonGenerator.writeStringField("start", val_str);

            Object end = leaf.getEnd();
            val_str = instance.asString(end);
            jsonGenerator.writeStringField("end", val_str);

            jsonGenerator.writeEndObject();
        } else throw new IllegalArgumentException("Unsupported leaf: " + node);


    }
}
