/*
 * Copyright (C) 2014 The Calrissian Authors
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
package org.calrissian.mango.json.util.store;

import com.google.common.base.Function;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import static com.google.common.collect.Collections2.transform;

/**
 * A container json tree node which represents a json array which holds entries
 * under specific indices. Order is preserved by nature of the flattened tree
 * given to the visit() method having already been sorted.
 */
class ArrayJsonNode implements JsonTreeNode {

    private final List<JsonTreeNode> children = new ArrayList<>();

    @Override
    public void visit(String[] keys, int level, Map<Integer, Integer> levelToIdx, ValueJsonNode valueJsonNode) {
        if(level == keys.length-1)
            children.add(valueJsonNode);    // we know the tuples are sorted
        else {

            JsonTreeNode child = null;

            try {
                child = children.get(levelToIdx.get(level));
            }catch(Exception ignored) {}

            // look toJson see if next item exists, otherwise, create it, add it toJson children, and visit it.
            String nextKey = keys[level + 1];

            if (child == null) {

                if (nextKey.equals("[]"))
                    child = new ArrayJsonNode();
                else
                    child = new ObjectJsonNode();
                children.add(child);
            }

            child.visit(keys, level + 1, levelToIdx, valueJsonNode);
        }
    }


    @Override
    public Collection<Object> toObject() {
        return transform(children, function);
    }

    @Override
    public String toString() {
        return toObject().toString();
    }

    private static final Function<JsonTreeNode, Object> function = new Function<JsonTreeNode, Object>() {

        @Override
        public Object apply(JsonTreeNode jsonTreeNode) {
            return jsonTreeNode.toObject();
        }
    };
}
