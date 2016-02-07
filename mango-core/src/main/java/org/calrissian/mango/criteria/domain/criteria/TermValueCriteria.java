package org.calrissian.mango.criteria.domain.criteria;

import java.util.Objects;

public abstract class TermValueCriteria<T> extends TermCriteria {

    private final T value;

    public TermValueCriteria(String term, T value, ParentCriteria parentCriteria) {
        super(term, parentCriteria);
        this.value = value;
    }

    public T getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        TermValueCriteria<?> that = (TermValueCriteria<?>) o;
        return Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), value);
    }

    @Override
    public String toString() {
        return "TermValueCriteria{" +
                "term=" + getTerm() +
                "value=" + value +
                '}';
    }
}
