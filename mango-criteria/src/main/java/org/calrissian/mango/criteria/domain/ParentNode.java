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
package org.calrissian.mango.criteria.domain;

import org.calrissian.mango.criteria.visitor.NodeVisitor;

import java.util.List;
import java.util.Objects;

import static java.util.Objects.requireNonNull;

public abstract class ParentNode implements Node {
    private static final long serialVersionUID = 1L;

    private final List<Node> nodes;
    private final ParentNode parent;

    public ParentNode(List<Node> nodes, ParentNode parent) {
        this.nodes = requireNonNull(nodes);
        this.parent = parent;
    }

    @Override
    public ParentNode parent() {
        return parent;
    }

    @Override
    public List<Node> children() {
        return nodes;
    }

    @Override
    public void addChild(Node node) {
        nodes.add(node);
    }

    @Override
    public void removeChild(Node node) {
        nodes.remove(node);
    }

    public List<Node> getNodes() {
        return nodes;
    }

    @Override
    public void accept(NodeVisitor visitor) {
        visitor.begin(this);
        for (Node node : nodes.toArray(new Node[nodes.size()])) {
            node.accept(visitor);
        }
        visitor.end(this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ParentNode that = (ParentNode) o;
        return Objects.equals(nodes, that.nodes);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nodes);
    }
}
