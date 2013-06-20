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

import org.calrissian.mango.criteria.visitor.NodeVisitor;

import java.util.ArrayList;
import java.util.List;

/**
 * Date: 11/9/12
 * Time: 1:52 PM
 */
public abstract class ParentNode implements Node {

    protected List<Node> nodes;
    protected ParentNode parent;

    public ParentNode() {
        nodes = new ArrayList<Node>();
    }

    public ParentNode(ParentNode parent, List<Node> nodes) {
        this.parent = parent;
        this.nodes = nodes;
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

    public void setNodes(List<Node> nodes) {
        this.nodes = nodes;
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

        if (nodes != null ? !nodes.equals(that.nodes) : that.nodes != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = nodes != null ? nodes.hashCode() : 0;
        return result;
    }
}
