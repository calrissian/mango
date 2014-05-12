package org.calrissian.mango.criteria.domain.criteria;

import org.calrissian.mango.types.TypeRegistry;
import org.calrissian.mango.types.exception.TypeEncodingException;

public abstract class AbstractLexiTypedKeyValueLeafCriteria extends AbstractKeyValueLeafCriteria{
  protected TypeRegistry<String> lexiTypes;

  protected String encodedVal;

  public AbstractLexiTypedKeyValueLeafCriteria(String key, Object value, TypeRegistry<String> lexiTypes,
                                               ParentCriteria parentCriteria) {

    super(key, value, parentCriteria);
    this.lexiTypes = lexiTypes;
    try {
      this.encodedVal = lexiTypes.encode(value);
    } catch (TypeEncodingException e) {
      throw new RuntimeException(e);
    }
  }
}
