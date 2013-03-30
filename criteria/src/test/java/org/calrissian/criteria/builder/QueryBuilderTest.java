package org.calrissian.criteria.builder;

import org.calrissian.criteria.domain.Node;
import org.calrissian.criteria.utils.NodeUtils;
import org.calrissian.criteria.visitor.PrintNodeVisitor;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.StringWriter;

import static org.junit.Assert.assertEquals;

public class QueryBuilderTest {
    private static final Logger logger = LoggerFactory.getLogger(QueryBuilderTest.class);

    @Test
    public void testBuildSimpleAndStatement() throws Exception {
        //and (eq, noteq, range)
        Node build = new QueryBuilder().and().eq("k1", "v1").notEq("k2", "v2").range("k3", "1", "2").endStatement().build();

        StringWriter writer = new StringWriter();
        build.accept(new PrintNodeVisitor(writer));

        assertEquals("AndNode(Equals[k1,v1],NotEquals[k2,v2],Range[k3,(1,2)],),", writer.toString());
    }

    @Test
    public void testEq() throws Exception {
        Node build = new QueryBuilder().eq("feedName", "netflowv9").build();
        String json = NodeUtils.serialize(build);
        System.out.println(json);
    }

    @Test
    public void testBuildComplexOrAndQuery() throws Exception {
        StringWriter writer = new StringWriter();
        Node node = new QueryBuilder().or().and().eq("k1", "v1").notEq("k2", "v2").range("k3", "1", "2").endStatement().and().eq("k4", "v4").endStatement().endStatement().build();
        node.accept(new PrintNodeVisitor(writer));
        assertEquals("OrNode(AndNode(Equals[k1,v1],NotEquals[k2,v2],Range[k3,(1,2)],),AndNode(Equals[k4,v4],),),", writer.toString());
    }
}
