package org.calrissian.mango.criteria.domain.criteria;

/**
 * Created by cjnolet on 5/9/14.
 */
public abstract class AbstractKeyValueLeafCriteria extends LeafCriteria {

  protected String key;
  protected Object value;

  public AbstractKeyValueLeafCriteria(String key, Object value, ParentCriteria parentCriteria) {
    super(parentCriteria);
    this.key = key;
    this.value = value;
  }

  public String getKey() {
    return key;
  }

  public Object getValue() {
    return value;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    AbstractKeyValueLeafCriteria that = (AbstractKeyValueLeafCriteria) o;

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

  @Override
  public String toString() {
    return "AbstractKeyValueLeafCriteria{" +
            "key='" + key + '\'' +
            ", value=" + value +
            ", parent=" + parent.getClass().getSimpleName() +
            '}';
  }
}
