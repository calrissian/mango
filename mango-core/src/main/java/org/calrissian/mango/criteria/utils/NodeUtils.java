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
import org.calrissian.mango.criteria.domain.criteria.*;

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

  public static Criteria criteriaFromNode(Node node) {
    return criteriaFromNode(node, null);
  }

  private static Criteria criteriaFromNode(Node node, ParentCriteria parent) {

    Criteria curNode = null;

    if(node instanceof AndNode)
      curNode = new AndCriteria(parent);
    else if(node instanceof OrNode)
      curNode = new OrCriteria(parent);
    else
      curNode = parseLeaf(node, parent);

    if(node.children() != null) {
      for(Node child : node.children()) {
        if(child instanceof Leaf)
          curNode.addChild(parseLeaf(child, (ParentCriteria)curNode));
        else
          curNode.addChild(criteriaFromNode(child, (ParentCriteria) curNode));
      }
    }
    if(parent != null)
      parent.addChild(curNode);

    return curNode;
  }

  private static Criteria parseLeaf(Node node, ParentCriteria parent) {
    AbstractKeyValueLeaf leaf = ((AbstractKeyValueLeaf)node);
    if(node instanceof EqualsLeaf)
      return new EqualsCriteria(leaf.getKey(), leaf.getValue(), parent);
    else if(node instanceof NotEqualsLeaf)
      return new NotEqualsCriteria(leaf.getKey(), leaf.getValue(), parent);
    else
      throw new IllegalArgumentException("An unsupported leaf was encountered: " + node.getClass());
  }

}
