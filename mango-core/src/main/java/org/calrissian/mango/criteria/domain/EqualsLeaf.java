package org.calrissian.mango.criteria.domain;

/**
 * Date: 11/9/12
 * Time: 1:49 PM
 */
public class EqualsLeaf extends Leaf {

    protected String key;
    protected Object value;

    public EqualsLeaf() {
        super(null);
    }

    public EqualsLeaf(String key, Object value, ParentNode parent) {
        super(parent);
        this.key = key;
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        EqualsLeaf that = (EqualsLeaf) o;

        if (key != null ? !key.equals(that.key) : that.key != null) return false;
        if (value != null ? !value.equals(that.value) : that.value != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = key != null ? key.hashCode() : 0;
        result = 31 * result + (value != null ? value.hashCode() : 0);
        return result;
    }
}
