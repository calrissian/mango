package org.calrissian.mango.criteria.visitor;

public interface OptimizerVisitor extends NodeVisitor {

  boolean optimized();
}
