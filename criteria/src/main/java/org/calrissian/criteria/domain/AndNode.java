package org.calrissian.criteria.domain;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Date: 11/9/12
 * Time: 1:53 PM
 */
public class AndNode extends ParentNode {
    private static final Logger logger = LoggerFactory.getLogger(AndNode.class);

    public AndNode() {
    }

    public AndNode(ParentNode parent, List<Node> nodes) {
        super(parent, nodes);
    }
}
