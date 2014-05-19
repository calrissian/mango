package org.calrissian.mango.criteria.domain;

public class HasLeaf extends AbstractKeyValueLeaf {
    public HasLeaf(String key, ParentNode parent) {
        super(key, null, parent);
    }

    @Override
    public Node clone(ParentNode node) {
        return new HasLeaf(key, node);
    }
}
