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

import org.calrissian.mango.criteria.domain.*;

import java.io.IOException;
import java.io.Writer;

/**
 * Date: 11/12/12
 * Time: 5:47 PM
 */
public class PrintNodeVisitor implements NodeVisitor {

    private Writer writer;

    public PrintNodeVisitor(Writer writer) {
        this.writer = writer;
    }

    @Override
    public void begin(ParentNode node) {
        write(node.getClass().getSimpleName() + "(");
    }

    @Override
    public void end(ParentNode node) {
        write("),");
        try {
            writer.flush();
        } catch (IOException e) {
        }
    }

    @Override
    public void visit(Leaf node) {
        if (node instanceof NotEqualsLeaf) {
            visit((NotEqualsLeaf) node);
        } else if (node instanceof EqualsLeaf) {
            visit((EqualsLeaf) node);
        } else if (node instanceof RangeLeaf) {
            visit((RangeLeaf) node);
        } else
            write(node.getClass().getSimpleName() + ",");
        try {
            writer.flush();
        } catch (IOException e) {
        }
    }

    public void visit(EqualsLeaf equalsLeaf) {
        write(String.format("Equals[%s,%s],", equalsLeaf.getKey(), equalsLeaf.getValue().toString()));
    }

    public void visit(NotEqualsLeaf notEqualsLeaf) {
        write(String.format("NotEquals[%s,%s],", notEqualsLeaf.getKey(), notEqualsLeaf.getValue().toString()));
    }

    public void visit(RangeLeaf range) {
        write(String.format("Range[%s,(%s,%s)],", range.getKey(), range.getStart().toString(), range.getEnd().toString()));
    }

    protected void write(String contents) {
        try {
            writer.write(contents);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
