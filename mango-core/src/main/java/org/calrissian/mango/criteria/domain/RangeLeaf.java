package org.calrissian.mango.criteria.domain;

/**
 * Date: 11/9/12
 * Time: 1:51 PM
 */
public class RangeLeaf extends Leaf {

    protected String key;
    protected Object start;
    protected Object end;

    public RangeLeaf() {
    }

    public RangeLeaf(String key, Object start, Object end, ParentNode parent) {
        super(parent);
        this.key = key;
        this.start = start;
        this.end = end;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Object getStart() {
        return start;
    }

    public void setStart(Object start) {
        this.start = start;
    }

    public Object getEnd() {
        return end;
    }

    public void setEnd(Object end) {
        this.end = end;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        RangeLeaf rangeLeaf = (RangeLeaf) o;

        if (end != null ? !end.equals(rangeLeaf.end) : rangeLeaf.end != null) return false;
        if (key != null ? !key.equals(rangeLeaf.key) : rangeLeaf.key != null) return false;
        if (start != null ? !start.equals(rangeLeaf.start) : rangeLeaf.start != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = (key != null ? key.hashCode() : 0);
        result = 31 * result + (start != null ? start.hashCode() : 0);
        result = 31 * result + (end != null ? end.hashCode() : 0);
        return result;
    }
}
