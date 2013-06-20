package org.calrissian.mango.criteria.domain;

import java.util.List;

/**
 * Date: 11/9/12
 * Time: 1:53 PM
 */
public class AndNode extends ParentNode {

    public AndNode() {
    }

    public AndNode(ParentNode parent, List<Node> nodes) {
        super(parent, nodes);
    }
}
