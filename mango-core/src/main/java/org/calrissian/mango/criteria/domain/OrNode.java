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
package org.calrissian.mango.criteria.domain;

import java.util.ArrayList;
import java.util.List;

public class OrNode extends ParentNode {
    private static final long serialVersionUID = 1L;

    public OrNode() {
    }

    public OrNode(ParentNode parent, List<Node> nodes) {
        super(parent, nodes);
    }

    public OrNode(ParentNode parent) {
        super(parent, new ArrayList<Node>());
    }

    @Override
    public Node clone(ParentNode node) {
        OrNode cloned = new OrNode(node);
        for (Node child : children())
            cloned.addChild(child.clone(cloned));
        return cloned;
    }
}
