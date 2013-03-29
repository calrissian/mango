package org.calrissian.mango.types.canonicalizer.domain;

public class CanonicalDef implements Comparable<CanonicalDef>{

    private final String type;
    private final String dataType;

    public CanonicalDef(String type, String dataType) {
        this.type = type;
        this.dataType = dataType;
    }

    public String getType() {
        return type;
    }

    public String getDataType() {
        return dataType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CanonicalDef)) return false;

        CanonicalDef that = (CanonicalDef) o;

        if (dataType != null ? !dataType.equals(that.dataType) : that.dataType != null) return false;
        if (type != null ? !type.equals(that.type) : that.type != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = type != null ? type.hashCode() : 0;
        result = 31 * result + (dataType != null ? dataType.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "CanonicalDef{" +
                "type='" + type + '\'' +
                ", dataType='" + dataType + '\'' +
                '}';
    }

    @Override
    public int compareTo(CanonicalDef canonicalDef) {
        return getType().compareTo(canonicalDef.getType());
    }
}
