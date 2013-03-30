package org.calrissian.criteria.visitor;

import org.calrissian.criteria.domain.Leaf;
import org.calrissian.criteria.domain.ParentNode;

/**
 * Date: 11/12/12
 * Time: 5:45 PM
 */
public interface NodeVisitor {

    public void begin(ParentNode node);

    public void end(ParentNode node);

    public void visit(Leaf node);
}
