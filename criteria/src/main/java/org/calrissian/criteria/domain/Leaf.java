package org.calrissian.criteria.domain;

import org.calrissian.criteria.visitor.NodeVisitor;

import java.util.List;

/**
 * Date: 11/9/12
 * Time: 1:46 PM
 */
public abstract class Leaf implements Node {

    private ParentNode parent;

    public Leaf() {
    }

    public Leaf(ParentNode parent) {
        this.parent = parent;
    }

    @Override
    public List<Node> children() {
        return null;
    }

    @Override
    public void addChild(Node node) {
        throw new UnsupportedOperationException("Leaf does not have children");
    }

    @Override
    public void accept(NodeVisitor visitor) {
        visitor.visit(this);
    }

    @Override
    public ParentNode parent() {
        return parent;
    }

    @Override
    public void removeChild(Node node) {
        throw new UnsupportedOperationException("Leaf does not have children");
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Leaf leaf = (Leaf) o;

        if (parent != null ? !parent.equals(leaf.parent) : leaf.parent != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return parent != null ? parent.hashCode() : 0;
    }
}
