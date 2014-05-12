package org.calrissian.mango.criteria.domain;

public class HasNotLeaf extends AbstractKeyValueLeaf{
  public HasNotLeaf(String key, ParentNode parent) {
    super(key, null, parent);
  }

  @Override
  public Node clone(ParentNode node) {
    return new HasNotLeaf(key, node);
  }
}
