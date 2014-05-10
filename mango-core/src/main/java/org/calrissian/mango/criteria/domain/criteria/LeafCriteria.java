package org.calrissian.mango.criteria.domain.criteria;

import java.util.List;

public abstract class LeafCriteria implements Criteria {

  protected List<Criteria> nodes;
  protected ParentCriteria parent;

  public LeafCriteria(ParentCriteria parentCriteria) {
    this.parent = parentCriteria;
  }

  @Override
  public ParentCriteria parent() {
    return parent;
  }

  @Override
  public List<Criteria> children() {
    return null;
  }

  @Override
  public void addChild(Criteria node) {
    throw new UnsupportedOperationException("Leaf does not have children");
  }


  @Override
  public void removeChild(Criteria node) {
    throw new UnsupportedOperationException("Leaf does not have children");
  }

  @Override
  public String toString() {
    return "LeafCriteria{" +
            "nodes=" + nodes +
            ", parent=" + parent +
            '}';
  }
}
