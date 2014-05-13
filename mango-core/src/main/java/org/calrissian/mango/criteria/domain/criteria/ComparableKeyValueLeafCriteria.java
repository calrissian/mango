package org.calrissian.mango.criteria.domain.criteria;

import java.util.Comparator;

public abstract class ComparableKeyValueLeafCriteria extends KeyValueLeafCriteria {
  protected Comparator comparator;

  public ComparableKeyValueLeafCriteria(String key, Object value, Comparator comparator,
                                        ParentCriteria parentCriteria) {
    super(key, value, parentCriteria);
    this.comparator = comparator;
  }
}
