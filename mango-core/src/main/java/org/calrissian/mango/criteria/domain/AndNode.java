/*
 * Copyright (C) 2017 The Calrissian Authors
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
import java.util.Objects;

import static java.util.stream.Collectors.joining;

public class AndNode extends ParentNode {
    private static final long serialVersionUID = 1L;

    public AndNode() {
        this(null);
    }

    public AndNode(ParentNode parent) {
        this(new ArrayList<>(), parent);
    }

    public AndNode(List<Node> nodes, ParentNode parent) {
        super(nodes, parent);
    }

    @Override
    public Node clone(ParentNode node) {
        AndNode cloned = new AndNode(node);
        for (Node child : children())
            cloned.addChild(child.clone(cloned));
        return cloned;
    }

    @Override
    public String toString() {
        return "(" + getNodes().stream().map(Objects::toString).collect(joining(" and ")) + ")";
    }
}
