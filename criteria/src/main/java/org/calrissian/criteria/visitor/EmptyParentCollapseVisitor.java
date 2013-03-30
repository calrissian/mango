package org.calrissian.criteria.visitor;

import org.calrissian.criteria.domain.Leaf;
import org.calrissian.criteria.domain.ParentNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A parent node with no children can be deleted.
 *
 * Date: 11/13/12
 * Time: 9:15 AM
 */
public class EmptyParentCollapseVisitor implements NodeVisitor {
    private static final Logger logger = LoggerFactory.getLogger(EmptyParentCollapseVisitor.class);

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
