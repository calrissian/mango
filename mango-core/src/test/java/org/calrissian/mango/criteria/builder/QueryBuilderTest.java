/*
 * Copyright (C) 2013 The Calrissian Authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.calrissian.mango.criteria.builder;

import org.calrissian.mango.criteria.domain.Node;
import org.calrissian.mango.criteria.visitor.PrintNodeVisitor;
import org.junit.Test;

import java.io.StringWriter;

import static org.junit.Assert.assertEquals;

public class QueryBuilderTest {

    @Test
    public void testBuildSimpleAndStatement() throws Exception {
        //and (eq, noteq, range)
        Node build = QueryBuilder.create().and().eq("k1", "v1").notEq("k2", "v2").range("k3", "1", "2").end().build();

        StringWriter writer = new StringWriter();
        build.accept(new PrintNodeVisitor(writer));

        assertEquals("AndNode(Equals[k1,v1],NotEquals[k2,v2],Range[k3,(1,2)],),", writer.toString());
    }

    @Test
    public void testEq() throws Exception {
        Node build = QueryBuilder.create().eq("feedName", "netflowv9").build();
        StringWriter writer = new StringWriter();
        build.accept(new PrintNodeVisitor(writer));
        assertEquals("AndNode(Equals[feedName,netflowv9],),", writer.toString());
    }

    @Test
    public void testBuildComplexOrAndQuery() throws Exception {
        StringWriter writer = new StringWriter();
        Node node = QueryBuilder.create().or().and().eq("k1", "v1").notEq("k2", "v2").range("k3", "1", "2").end().and().eq("k4", "v4").end().end().build();
        node.accept(new PrintNodeVisitor(writer));
        assertEquals("OrNode(AndNode(Equals[k1,v1],NotEquals[k2,v2],Range[k3,(1,2)],),AndNode(Equals[k4,v4],),),", writer.toString());
    }
}
