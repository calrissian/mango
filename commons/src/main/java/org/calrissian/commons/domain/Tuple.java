package org.calrissian.commons.domain;


import org.calrissian.commons.serialization.ObjectMapperContext;
import org.codehaus.jackson.map.ObjectMapper;

public class Tuple {

    protected final String key;
    protected final Object value;
    protected final String visibility;



    public Tuple(String key, Object value, String visibility) {
        this.key = key;
        this.value = value;
        this.visibility = visibility;
    }

    public String getKey() {
        return key;
    }

    public Object getValue() {
        return value;
    }

    public String getVisibility() {
        return visibility;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Tuple)) return false;

        Tuple tuple = (Tuple) o;

        if (key != null ? !key.equals(tuple.key) : tuple.key != null) return false;
        if (value != null ? !value.equals(tuple.value) : tuple.value != null) return false;
        if (visibility != null ? !visibility.equals(tuple.visibility) : tuple.visibility != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = key != null ? key.hashCode() : 0;
        result = 31 * result + (value != null ? value.hashCode() : 0);
        result = 31 * result + (visibility != null ? visibility.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Tuple{" +
                "key='" + key + '\'' +
                ", value=" + value +
                ", type=" + value.getClass() +
                ", visibility='" + visibility + '\'' +
                '}';
    }
}
