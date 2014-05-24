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
package org.calrissian.mango.criteria.visitor;

import org.calrissian.mango.criteria.domain.Leaf;
import org.calrissian.mango.criteria.domain.Node;
import org.calrissian.mango.criteria.domain.ParentNode;

/**
 * Multiple And/Or descending down the tree can be rolled up. And(And(children)) becomes And(children).
 * Requires that the parent classes be the same.
 *
 * Date: 11/13/12
 * Time: 9:56 AM
 */
public class CollapseParentClauseVisitor implements OptimizerVisitor {

    boolean wasOptimized = false;

    @Override
    public void begin(ParentNode node) {
        Class<? extends ParentNode> clause = node.getClass();
        if (node.parent() != null) {
            Class<? extends ParentNode> parentClause = node.parent().getClass();
            if (clause.equals(parentClause)) {
                //all children go to parent
                for (Node child : node.children()) {
                    node.parent().addChild(child);
                }
            }
        }
    }

    @Override
    public void end(ParentNode node) {

    }

    @Override
    public void visit(Leaf node) {

    }

  @Override
  public boolean optimized() {
    return wasOptimized;
  }
}
