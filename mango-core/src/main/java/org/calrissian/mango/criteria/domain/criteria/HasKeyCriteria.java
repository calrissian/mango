package org.calrissian.mango.criteria.domain.criteria;


import org.calrissian.mango.domain.TupleCollection;

public class HasKeyCriteria extends AbstractKeyValueLeafCriteria{
  public HasKeyCriteria(String key, ParentCriteria parentCriteria) {
    super(key, null, parentCriteria);
  }

  @Override
  public boolean matches(TupleCollection obj) {
    return obj.get(key) != null;
  }


}
