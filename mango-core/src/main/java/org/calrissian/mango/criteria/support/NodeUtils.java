/*
 * Copyright (C) 2014 The Calrissian Authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.calrissian.mango.criteria.support;

import org.calrissian.mango.criteria.domain.*;
import org.calrissian.mango.criteria.domain.criteria.*;

import java.util.Comparator;

import static com.google.common.collect.Ordering.natural;

public class NodeUtils {

    public NodeUtils() {/* private constructor */}

    /**
     * Returns true if a node is a leaf. Note that a leaf can also be a parent node that does not have any children.
     */
    public static boolean isLeaf(Node node) {
        return node instanceof Leaf || node.children() == null || node.children().size() == 0;
    }

    /**
     * Determines if a node is null, or a single parent node without children.
     */
    public static boolean isEmpty(Node node) {
        return (node == null || (node.children() != null && node.children().size() == 0));
    }

    /**
     * Determines if parent node has children that are all leaves
     */
    public static boolean parentContainsOnlyLeaves(ParentNode parentNode) {
        for (Node child : parentNode.children()) {
            if (!isLeaf(child)) return false;
        }
        return true;
    }

    /**
     * Determines if leaf represents a possible range of values (bounded or unbounded)
     */
    public static boolean isRangeLeaf(Leaf leaf) {
        return leaf instanceof RangeLeaf || leaf instanceof GreaterThanEqualsLeaf || leaf instanceof GreaterThanLeaf ||
                leaf instanceof LessThanLeaf || leaf instanceof LessThanEqualsLeaf;
    }

    /**
     * Creates criteria from a node. A default comparator is used which assumes values implement Comparable.
     */
    public static Criteria criteriaFromNode(Node node) {
        return criteriaFromNode(node, natural(), null);
    }

    /**
     * Creates criteria from a node. A Comparator is injected into all nodes which need to determine order or equality.
     */
    public static Criteria criteriaFromNode(Node node, Comparator rangeComparator) {
        return criteriaFromNode(node, rangeComparator, null);
    }

    private static Criteria criteriaFromNode(Node node, Comparator rangeComparator, ParentCriteria parent) {

        Criteria curNode;

        if (node instanceof AndNode)
            curNode = new AndCriteria(parent);
        else if (node instanceof OrNode)
            curNode = new OrCriteria(parent);
        else
            curNode = parseLeaf(node, rangeComparator, parent);

        if (node.children() != null) {
            for (Node child : node.children()) {
                if (child instanceof Leaf)
                    curNode.addChild(parseLeaf(child, rangeComparator, (ParentCriteria) curNode));
                else
                    curNode.addChild(criteriaFromNode(child, rangeComparator, (ParentCriteria) curNode));
            }
        }
        if (parent != null)
            parent.addChild(curNode);

        return curNode;
    }

    private static Criteria parseLeaf(Node node, Comparator rangeComparator, ParentCriteria parent) {

        if (node instanceof TypedTermLeaf) {
            if (node instanceof TermValueLeaf) {
                TermValueLeaf leaf = (TermValueLeaf) node;
                if (node instanceof EqualsLeaf)
                    return new EqualsCriteria<>(leaf.getTerm(), leaf.getValue(), rangeComparator, parent);
                else if (node instanceof NotEqualsLeaf)
                    return new NotEqualsCriteria<>(leaf.getTerm(), leaf.getValue(), rangeComparator, parent);
                else if (node instanceof LessThanLeaf)
                    return new LessThanCriteria<>(leaf.getTerm(), leaf.getValue(), rangeComparator, parent);
                else if (node instanceof LessThanEqualsLeaf)
                    return new LessThanEqualsCriteria<>(leaf.getTerm(), leaf.getValue(), rangeComparator, parent);
                else if (node instanceof GreaterThanLeaf)
                    return new GreaterThanCriteria<>(leaf.getTerm(), leaf.getValue(), rangeComparator, parent);
                else if (node instanceof GreaterThanEqualsLeaf)
                    return new GreaterThanEqualsCriteria<>(leaf.getTerm(), leaf.getValue(), rangeComparator, parent);
            } else if (node instanceof RangeLeaf) {
                RangeLeaf rangeLeaf = (RangeLeaf) node;
                return new RangeCriteria<>(rangeLeaf.getTerm(), rangeLeaf.getStart(), rangeLeaf.getEnd(), rangeComparator, parent);
            } else if (node instanceof HasLeaf) {
                HasLeaf leaf = (HasLeaf) node;
                return new HasCriteria<>(leaf.getTerm(), leaf.getType(), parent);
            } else if (node instanceof HasNotLeaf) {
                HasNotLeaf leaf = (HasNotLeaf) node;
                return new HasNotCriteria<>(leaf.getTerm(), leaf.getType(), parent);
            }
        }

        throw new IllegalArgumentException("An unsupported leaf was encountered: " + node.getClass());
    }

}
