package org.calrissian.mango.criteria.domain.criteria;

import org.calrissian.mango.domain.TupleCollection;

import java.util.List;

public interface Criteria {

  boolean matches(TupleCollection obj);

  public ParentCriteria parent();

  public List<Criteria> children();

  public void addChild(Criteria node);

  public void removeChild(Criteria node);
}
