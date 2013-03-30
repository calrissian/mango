package org.calrissian.criteria.domain;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Date: 11/9/12
 * Time: 1:57 PM
 */
public class NotEqualsLeaf extends Leaf {
    private static final Logger logger = LoggerFactory.getLogger(EqualsLeaf.class);

    protected String key;
    protected Object value;

    public NotEqualsLeaf() {
        super(null);
    }

    public NotEqualsLeaf(String key, Object value, ParentNode parent) {
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

        NotEqualsLeaf that = (NotEqualsLeaf) o;

        if (key != null ? !key.equals(that.key) : that.key != null) return false;
        if (value != null ? !value.equals(that.value) : that.value != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = (key != null ? key.hashCode() : 0);
        result = 31 * result + (value != null ? value.hashCode() : 0);
        return result;
    }
}
