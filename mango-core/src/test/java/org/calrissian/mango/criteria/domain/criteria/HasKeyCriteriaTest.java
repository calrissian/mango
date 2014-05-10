package org.calrissian.mango.criteria.domain.criteria;

import org.calrissian.mango.domain.BaseEntity;
import org.calrissian.mango.domain.Entity;
import org.calrissian.mango.domain.Tuple;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class HasKeyCriteriaTest {

  @Test
  public void test() {

    HasKeyCriteria criteria = new HasKeyCriteria("key1", null);

    Entity entity = new BaseEntity("type", "id");
    entity.put(new Tuple("key2", "val2"));

    assertFalse(criteria.matches(entity));

    entity.put(new Tuple("key1", "val1"));
    assertTrue(criteria.matches(entity));
  }
}
