package org.calrissian.mango.criteria.domain.criteria;

import org.calrissian.mango.domain.Tuple;
import org.calrissian.mango.domain.TupleCollection;
import org.calrissian.mango.types.TypeRegistry;
import org.calrissian.mango.types.exception.TypeEncodingException;

import java.util.Collection;

public class GreaterThanEqualsCriteria extends AbstractLexiTypedKeyValueLeafCriteria {
  public GreaterThanEqualsCriteria(String key, Object value, TypeRegistry<String> lexiTypes, ParentCriteria parentCriteria) {
    super(key, value, lexiTypes, parentCriteria);
  }

  @Override
  public boolean matches(TupleCollection obj) {
    Collection<Tuple> tuples = obj.getAll(key);
    if(tuples != null) {
      for(Tuple tuple : tuples) {
        try {
          return lexiTypes.encode(tuple.getValue()).compareTo(encodedVal) >= 0;
        } catch (TypeEncodingException e) {
          throw new RuntimeException(e);
        }
      }
    }

    return false;
  }
}
