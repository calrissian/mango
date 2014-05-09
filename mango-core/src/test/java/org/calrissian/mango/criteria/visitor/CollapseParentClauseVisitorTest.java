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
        Node node = new QueryBuilder().and().and().eq("k1", "v1").eq("k2", "v2").end().end().build();
        node.accept(new PrintNodeVisitor(new OutputStreamWriter(System.out)));
        System.out.println();
        node.accept(new CollapseParentClauseVisitor());
        node.accept(new PrintNodeVisitor(new OutputStreamWriter(System.out)));
        System.out.println();
    }

    @Test
    public void testCollapseAndAndOrChildren() throws Exception {
        Node node = new QueryBuilder().and().and().eq("k1", "v1").eq("k2", "v2").end().or().eq("k3", "v3").eq("k4", "v4").end().end().build();
        node.accept(new PrintNodeVisitor(new OutputStreamWriter(System.out)));
        System.out.println();
        node.accept(new CollapseParentClauseVisitor());
        node.accept(new PrintNodeVisitor(new OutputStreamWriter(System.out)));
        System.out.println();
    }
}

