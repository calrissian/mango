package org.calrissian.mango.criteria.domain;


import java.util.Objects;

public abstract class TermLeaf extends Leaf {

    private final String term;

    public TermLeaf(String term, ParentNode parent) {
        super(parent);
        this.term = term;
    }

    public String getTerm() {
        return term;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        TermLeaf that = (TermLeaf) o;
        return Objects.equals(term, that.term);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), term);
    }
}
