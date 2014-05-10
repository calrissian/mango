package org.calrissian.mango.criteria.domain.criteria;

import org.calrissian.mango.domain.Tuple;
import org.calrissian.mango.domain.TupleCollection;

import java.util.Collection;

public class NotEqualsCriteria extends AbstractKeyValueLeafCriteria {

  public NotEqualsCriteria(String key, Object value, ParentCriteria parentCriteria) {
    super(key, value, parentCriteria);
  }

  @Override
  public boolean matches(TupleCollection obj) {
    Collection<Tuple> tuples = obj.getAll(key);
    if(tuples != null) {
      for(Tuple tuple : tuples) {
        if(tuple.getValue() != null && tuple.getValue().equals(value))
          return false;
      }
    }

    return true;
  }
}
