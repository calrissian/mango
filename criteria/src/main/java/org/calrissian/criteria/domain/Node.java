package org.calrissian.criteria.domain;

import org.calrissian.criteria.visitor.NodeVisitor;

import java.io.Serializable;
import java.util.List;

/**
 * Date: 11/9/12
 * Time: 1:47 PM
 */
//@JsonTypeInfo(use=JsonTypeInfo.Id.CLASS, include= JsonTypeInfo.As.PROPERTY, property="@class")
public interface Node extends Serializable {

    public ParentNode parent();

    public List<Node> children();

    public void addChild(Node node);

    public void removeChild(Node node);

    public void accept(NodeVisitor visitor);
}
