package org.calrissian.mango.criteria.utils;

import org.calrissian.mango.criteria.builder.QueryBuilder;
import org.calrissian.mango.criteria.domain.Node;
import org.calrissian.mango.criteria.domain.criteria.AndCriteria;
import org.calrissian.mango.criteria.domain.criteria.Criteria;
import org.calrissian.mango.criteria.domain.criteria.EqualsCriteria;
import org.calrissian.mango.criteria.domain.criteria.OrCriteria;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class NodeUtilsTest {

  @Test
  public void testCriteriaFromNode_fullTree() {

    Node node = new QueryBuilder().or().and().eq("key1", "val1").eq("key2", "val2").end().eq("key3", "val3").end().build();

    Criteria criteria = NodeUtils.criteriaFromNode(node);

    assertTrue(criteria instanceof OrCriteria);
    assertTrue(criteria.children().get(0) instanceof AndCriteria);
    assertTrue(criteria.children().get(0).children().get(0) instanceof EqualsCriteria);
    assertTrue(criteria.children().get(0).children().get(1) instanceof EqualsCriteria);
    assertTrue(criteria.children().get(1).children().get(0) instanceof EqualsCriteria);
  }
}
