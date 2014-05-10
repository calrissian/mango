package org.calrissian.mango.criteria.domain.criteria;

import org.calrissian.mango.domain.TupleCollection;

public class AndCriteria extends ParentCriteria{

  public AndCriteria(ParentCriteria parent) {
    super(parent);
  }

  @Override
  public boolean matches(TupleCollection obj) {
    for(Criteria node : children()) {
      if(!node.matches(obj))
        return false;
    }

    return true;
  }


}
