package org.calrissian.mango.criteria.domain.criteria;

import org.calrissian.mango.domain.TupleCollection;

public class OrCriteria extends ParentCriteria{

  public OrCriteria(ParentCriteria parent) {
    super(parent);
  }

  @Override
  public boolean matches(TupleCollection obj) {
    for(Criteria node : children()) {
      if(node.matches(obj))
        return true;
    }

    return false;
  }
}
