package org.calrissian.criteria.domain;

import org.calrissian.criteria.domain.Node;
import org.calrissian.criteria.domain.ParentNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Date: 11/9/12
 * Time: 1:54 PM
 */
public class OrNode extends ParentNode {
    private static final Logger logger = LoggerFactory.getLogger(OrNode.class);

    public OrNode() {
    }

    public OrNode(ParentNode parent, List<Node> nodes) {
        super(parent, nodes);
    }
}
