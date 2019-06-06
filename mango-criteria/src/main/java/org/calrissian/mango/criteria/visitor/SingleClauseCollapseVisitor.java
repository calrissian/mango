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
package org.calrissian.mango.criteria.visitor;

import org.calrissian.mango.criteria.domain.Leaf;
import org.calrissian.mango.criteria.domain.ParentNode;

/**
 * If the parent has only one child, the child can be rolled up into the parent's parent. (if it exists)
 */
public class SingleClauseCollapseVisitor implements NodeVisitor {

    @Override
    public void begin(ParentNode node) {
        if (node.children() != null && node.children().size() == 1) {
            if (node.parent() != null) {
                node.parent().addChild(node.children().get(0));
                node.parent().removeChild(node);
            }
        }
    }

    @Override
    public void end(ParentNode node) {

    }

    @Override
    public void visit(Leaf node) {

    }

}
