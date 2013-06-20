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
