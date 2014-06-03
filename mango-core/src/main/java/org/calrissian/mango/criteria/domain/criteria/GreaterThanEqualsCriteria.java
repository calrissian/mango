package org.calrissian.mango.criteria.domain.criteria;

import org.calrissian.mango.domain.Tuple;
import org.calrissian.mango.domain.TupleStore;

import java.util.Collection;
import java.util.Comparator;

public class GreaterThanEqualsCriteria extends ComparableKeyValueLeafCriteria {
    public GreaterThanEqualsCriteria(String key, Object value, Comparator comparator, ParentCriteria parentCriteria) {
        super(key, value, comparator, parentCriteria);
    }


    @Override
    public Criteria clone(ParentCriteria parentCriteria) {
        return new GreaterThanEqualsCriteria(key, value, comparator, parentCriteria);
    }

    @Override
    public boolean apply(TupleStore obj) {
        Collection<Tuple> tuples = obj.getAll(key);
        if (tuples != null) {
            for (Tuple tuple : tuples)
                if(comparator.compare(tuple.getValue(), value) >= 0)
                    return true;
        }

        return false;
    }
}
