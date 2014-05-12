package org.calrissian.mango.criteria.domain.criteria;

import org.calrissian.mango.domain.Tuple;
import org.calrissian.mango.domain.TupleCollection;
import org.calrissian.mango.types.TypeRegistry;
import org.calrissian.mango.types.exception.TypeEncodingException;

import java.util.Collection;

public class RangeCriteria extends AbstractLexiTypedKeyValueLeafCriteria {

  protected Object end;
  protected String encodedEnd;

  public RangeCriteria(String key, Object start, Object end, TypeRegistry<String> lexiTypes, ParentCriteria parentCriteria) {
    super(key, start, lexiTypes, parentCriteria);
    this.end = end;
    try {
      this.encodedEnd = lexiTypes.encode(end);
    } catch (TypeEncodingException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public boolean matches(TupleCollection obj) {
    Collection<Tuple> tuples = obj.getAll(key);
    if(tuples != null) {
      for(Tuple tuple : tuples) {
        try {
          String objEncoded = lexiTypes.encode(tuple.getValue());
          return objEncoded.compareTo(encodedVal) >= 0 && objEncoded.compareTo(encodedEnd) <= 0;
        } catch (TypeEncodingException e) {
          throw new RuntimeException(e);
        }
      }
    }

    return false;
  }
}
