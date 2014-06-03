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
    public Criteria clone(ParentCriteria parentCriteria) {
        return new RangeCriteria(key, value, end, comparator, parentCriteria);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        RangeCriteria that = (RangeCriteria) o;

        if (encodedEnd != null ? !encodedEnd.equals(that.encodedEnd) : that.encodedEnd != null) return false;
        if (end != null ? !end.equals(that.end) : that.end != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (end != null ? end.hashCode() : 0);
        result = 31 * result + (encodedEnd != null ? encodedEnd.hashCode() : 0);
        return result;
    }

    @Override
    public boolean apply(TupleStore obj) {
        Collection<Tuple> tuples = obj.getAll(key);
        if (tuples != null) {
            for (Tuple tuple : tuples) {
                int startCompare = comparator.compare(tuple.getValue(), value);
                int endCompare = comparator.compare(tuple.getValue(), end);
                if(startCompare >= 0 && endCompare <= 0)
                    return true;
            }
        }

        return false;
    }
}
