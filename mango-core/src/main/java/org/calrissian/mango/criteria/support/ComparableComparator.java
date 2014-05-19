package org.calrissian.mango.criteria.support;

import java.io.Serializable;
import java.util.Comparator;

public class ComparableComparator implements Comparator<Comparable>, Serializable {
    @Override
    public int compare(Comparable comparable, Comparable comparable2) {
        return comparable.compareTo(comparable2);
    }
}
