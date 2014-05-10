package org.calrissian.mango.criteria.domain.criteria;

import org.calrissian.mango.domain.BaseEntity;
import org.calrissian.mango.domain.Entity;
import org.calrissian.mango.domain.Tuple;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class NotEqualsCriteriaTest {

  @Test
  public void testNotEquals() {

    Criteria eq = new NotEqualsCriteria("key1", "val1", null);
    Entity entity = new BaseEntity("type", "id");
    entity.put(new Tuple("key1", "val2"));

    assertTrue(eq.matches(entity));
  }

  @Test
  public void testEquals() {

    Criteria eq = new NotEqualsCriteria("key1", "val1", null);
    Entity entity = new BaseEntity("type", "id");
    entity.put(new Tuple("key1", "val1"));

    assertFalse(eq.matches(entity));

  }
}
