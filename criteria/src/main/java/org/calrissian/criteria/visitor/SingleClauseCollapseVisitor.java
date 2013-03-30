package org.calrissian.criteria.visitor;

import org.calrissian.criteria.domain.Leaf;
import org.calrissian.criteria.domain.ParentNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * If the parent has only one child, the child can be rolled up into the parent's parent. (if it exists)
 *
 * Date: 11/13/12
 * Time: 9:30 AM
 */
public class SingleClauseCollapseVisitor implements NodeVisitor {
    private static final Logger logger = LoggerFactory.getLogger(SingleClauseCollapseVisitor.class);

    @Override
    public void begin(ParentNode node) {
        if (node.children() != null && node.children().size() == 1) {
            if (node.parent() != null) {
                node.parent().addChild(node.children().get(0));
                node.parent().removeChild(node);
            }
        }
    }

    @Override
    public void end(ParentNode node) {

    }

    @Override
    public void visit(Leaf node) {

    }
}
