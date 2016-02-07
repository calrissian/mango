package org.calrissian.mango.criteria.domain;


import java.util.Objects;

public abstract class TypedTermLeaf<T> extends TermLeaf {

    //Utility method to allow subclasses to extract type from known variables.
    protected static <T> Class<T> firstKnownType(T... objects) {
        for (T obj : objects)
            if (obj != null)
                return (Class<T>) obj.getClass();
        return null;
    }

    private final Class<T> type;

    public TypedTermLeaf(String term, Class<T> type, ParentNode parent) {
        super(term, parent);
        this.type = type;
    }

    public Class<T> getType() {
        return type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        TypedTermLeaf<?> that = (TypedTermLeaf<?>) o;
        return Objects.equals(type, that.type);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), type);
    }
}
