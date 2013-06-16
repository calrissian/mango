package org.calrissian.mango.criteria.domain;

import java.util.List;

/**
 * Date: 11/9/12
 * Time: 1:54 PM
 */
public class OrNode extends ParentNode {

    public OrNode() {
    }

    public OrNode(ParentNode parent, List<Node> nodes) {
        super(parent, nodes);
    }
}
