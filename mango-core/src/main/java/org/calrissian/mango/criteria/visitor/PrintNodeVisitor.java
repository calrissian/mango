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
