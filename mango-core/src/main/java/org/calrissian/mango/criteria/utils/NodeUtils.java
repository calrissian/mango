package org.calrissian.mango.criteria.utils;

import org.calrissian.mango.criteria.domain.Leaf;
import org.calrissian.mango.criteria.domain.Node;
import org.calrissian.mango.criteria.domain.ParentNode;
import org.calrissian.mango.criteria.serialization.NodeMixin;
import org.calrissian.mango.serialization.ObjectMapperContext;
import org.codehaus.jackson.map.ObjectMapper;

import java.io.IOException;

public class NodeUtils {

    static {
        ObjectMapper objectMapper = ObjectMapperContext.getInstance().getObjectMapper();

        objectMapper.getSerializationConfig().addMixInAnnotations(Node.class, NodeMixin.class);
        objectMapper.getDeserializationConfig().addMixInAnnotations(Node.class, NodeMixin.class);
    }

    public static boolean isLeaf(Node node) {
        return node instanceof Leaf || node.children() == null || node.children().size() == 0;
    }

    public static boolean parentContainsOnlyLeaves(ParentNode parentNode) {
        for (Node child : parentNode.children()) {
            if (!isLeaf(child)) return false;
        }
        return true;
    }

    public static String serialize(Node node) throws IOException {
        return ObjectMapperContext.getInstance().getObjectMapper().writeValueAsString(node);
    }

    public static Node deserialize(String json) throws IOException {
        return ObjectMapperContext.getInstance().getObjectMapper().readValue(json, Node.class);
    }

    public static void initializeMixins() {
        //that static method takes care of this
    }
}
