package org.calrissian.mango.criteria.visitor;

import org.calrissian.mango.criteria.domain.Leaf;
import org.calrissian.mango.criteria.domain.ParentNode;

/**
 * A parent node with no children can be deleted.
 *
 * Date: 11/13/12
 * Time: 9:15 AM
 */
public class EmptyParentCollapseVisitor implements NodeVisitor {

    @Override
    public void begin(ParentNode node) {
        if (node.children() == null || node.children().size() == 0) {
            if (node.parent() != null) node.parent().removeChild(node);
        }
    }

    @Override
    public void end(ParentNode node) {

    }

    @Override
    public void visit(Leaf node) {

    }
}
