package org.calrissian.mango.criteria.domain.criteria;

import org.calrissian.mango.domain.Tuple;
import org.calrissian.mango.domain.TupleCollection;

import java.util.Collection;
import java.util.Comparator;

public class LessThanCriteria extends ComparableKeyValueLeafCriteria {
    public LessThanCriteria(String key, Object value, Comparator comparator, ParentCriteria parentCriteria) {
        super(key, value, comparator, parentCriteria);
    }

    @Override
    public boolean apply(TupleCollection obj) {
        Collection<Tuple> tuples = obj.getAll(key);
        if (tuples != null) {
            for (Tuple tuple : tuples)
                return comparator.compare(tuple.getValue(), value) < 0;
        }

        return false;
    }
}
