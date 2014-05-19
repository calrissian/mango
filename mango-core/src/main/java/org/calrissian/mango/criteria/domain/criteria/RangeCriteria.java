package org.calrissian.mango.criteria.domain.criteria;

import org.calrissian.mango.domain.Tuple;
import org.calrissian.mango.domain.TupleStore;

import java.util.Collection;
import java.util.Comparator;

public class RangeCriteria extends ComparableKeyValueLeafCriteria {

    protected Object end;
    protected String encodedEnd;

    public RangeCriteria(String key, Object start, Object end, Comparator comparator, ParentCriteria parentCriteria) {
        super(key, start, comparator, parentCriteria);
        this.end = end;
    }

    @Override
    public boolean apply(TupleStore obj) {
        Collection<Tuple> tuples = obj.getAll(key);
        if (tuples != null) {
            for (Tuple tuple : tuples) {
                int startCompare = comparator.compare(tuple.getValue(), value);
                int endCompare = comparator.compare(tuple.getValue(), end);
                return startCompare >= 0 && endCompare <= 0;
            }
        }

        return false;
    }
}
