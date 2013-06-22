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
import org.calrissian.mango.criteria.serialization.CriteriaModule;
import org.calrissian.mango.types.TypeContext;
import org.codehaus.jackson.map.ObjectMapper;

import java.io.IOException;

import static org.calrissian.mango.types.TypeContext.DEFAULT_TYPES;

public class NodeUtils {

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
        return serialize(node, DEFAULT_TYPES);
    }

    public static String serialize(Node node, TypeContext typeContext) throws IOException {
        return new ObjectMapper().withModule(new CriteriaModule(typeContext)).writeValueAsString(node);
    }

    public static Node deserialize(String json) throws IOException {
        return deserialize(json, DEFAULT_TYPES);
    }

    public static Node deserialize(String json, TypeContext typeContext) throws IOException {
        return new ObjectMapper().withModule(new CriteriaModule(typeContext)).readValue(json, Node.class);
    }
}
