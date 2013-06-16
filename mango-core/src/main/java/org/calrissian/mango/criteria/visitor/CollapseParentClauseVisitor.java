package org.calrissian.mango.criteria.visitor;

import org.calrissian.mango.criteria.domain.Leaf;
import org.calrissian.mango.criteria.domain.Node;
import org.calrissian.mango.criteria.domain.ParentNode;

/**
 * Multiple And/Or descending down the tree can be rolled up. And(And(children)) becomes And(children).
 * Requires that the parent classes be the same.
 *
 * Date: 11/13/12
 * Time: 9:56 AM
 */
public class CollapseParentClauseVisitor implements NodeVisitor {

    @Override
    public void begin(ParentNode node) {
        Class<? extends ParentNode> clause = node.getClass();
        if (node.parent() != null) {
            Class<? extends ParentNode> parentClause = node.parent().getClass();
            if (clause.equals(parentClause)) {
                //all children go to parent
                for (Node child : node.children()) {
                    node.parent().addChild(child);
                }
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
