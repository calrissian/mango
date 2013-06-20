package org.calrissian.mango.criteria.visitor;


import org.calrissian.mango.criteria.builder.QueryBuilder;
import org.calrissian.mango.criteria.domain.Node;
import org.junit.Test;

import java.io.OutputStreamWriter;

/**
 * Date: 11/13/12
 * Time: 3:20 PM
 */
public class CollapseParentClauseVisitorTest {

    @Test
    public void testCollapseAndAndChildren() throws Exception {
        Node node = new QueryBuilder().and().and().eq("k1", "v1").eq("k2", "v2").endStatement().endStatement().build();
        node.accept(new PrintNodeVisitor(new OutputStreamWriter(System.out)));
        System.out.println();
        node.accept(new CollapseParentClauseVisitor());
        node.accept(new PrintNodeVisitor(new OutputStreamWriter(System.out)));
        System.out.println();
    }

    @Test
    public void testCollapseAndAndOrChildren() throws Exception {
        Node node = new QueryBuilder().and().and().eq("k1", "v1").eq("k2", "v2").endStatement().or().eq("k3", "v3").eq("k4", "v4").endStatement().endStatement().build();
        node.accept(new PrintNodeVisitor(new OutputStreamWriter(System.out)));
        System.out.println();
        node.accept(new CollapseParentClauseVisitor());
        node.accept(new PrintNodeVisitor(new OutputStreamWriter(System.out)));
        System.out.println();
    }
}

