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

import org.calrissian.mango.criteria.domain.*;

public class NodeUtils {

    public NodeUtils() {/* private constructor */}

    public static boolean isLeaf(Node node) {
        return node instanceof Leaf || node.children() == null || node.children().size() == 0;
    }

    public static boolean parentContainsOnlyLeaves(ParentNode parentNode) {
        for (Node child : parentNode.children()) {
            if (!isLeaf(child)) return false;
        }
        return true;
    }

    public static boolean isRangeLeaf(Leaf leaf) {
      return leaf instanceof RangeLeaf || leaf instanceof GreaterThanEqualsLeaf || leaf instanceof GreaterThanLeaf ||
              leaf instanceof LessThanLeaf || leaf instanceof LessThanEqualsLeaf;
    }
}
