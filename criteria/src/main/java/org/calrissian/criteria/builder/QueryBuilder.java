package org.calrissian.criteria.builder;

import org.calrissian.criteria.domain.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;

/**
 * Date: 11/9/12
 * Time: 2:01 PM
 */
public class QueryBuilder {
    private static final Logger logger = LoggerFactory.getLogger(QueryBuilder.class);

    protected ParentNode current;
    protected QueryBuilder parentBuilder;
    protected boolean finished = false;

    public QueryBuilder() {
    }

    public QueryBuilder(ParentNode current, QueryBuilder parentBuilder) {
        this.current = current;
        this.parentBuilder = parentBuilder;
    }

    public QueryBuilder and() {
        checkFinished();
        AndNode andNode = new AndNode(current, new ArrayList<Node>());
        if (current != null) {
            current.addChild(andNode);
            return new QueryBuilder(andNode, this);
        }
        current = andNode;
        return new QueryBuilder(andNode, null);
    }

    public QueryBuilder or() {
        checkFinished();
        OrNode orNode = new OrNode(current, new ArrayList<Node>());
        if (current != null) {
            current.addChild(orNode);
            return new QueryBuilder(orNode, this);
        }
        current = orNode;
        return new QueryBuilder(orNode, null);
    }

    public Node build() {
        if (!finished) throw new IllegalArgumentException("Query Node not built, endStatement first");
        return current;
    }

    public QueryBuilder eq(String type, Object value) {
        checkFinished();
        EqualsLeaf equalsLeaf = new EqualsLeaf(type, value, current);
        if (this.current == null) {
            //single statement, can't add anymore
            this.current = new AndNode();
            finished = true;
        }
        this.current.addChild(equalsLeaf);
        return this;
    }

    protected void checkFinished() {
        if (finished)
            throw new IllegalArgumentException("Builder finished. Call build() to get constructed Query Node");
    }

    public QueryBuilder notEq(String type, Object value) {
        checkFinished();
        NotEqualsLeaf notEqualsLeaf = new NotEqualsLeaf(type, value, current);
        if (this.current == null) {
            //single statement, can't add anymore
            this.current = new AndNode();
            finished = true;
        }
        this.current.addChild(notEqualsLeaf);
        return this;
    }

    public QueryBuilder range(String type, Object start, Object end) {
        checkFinished();
        RangeLeaf rangeLeaf = new RangeLeaf(type, start, end, current);
        if (this.current == null) {
            //single statement, can't add anymore
            this.current = new AndNode();
            finished = true;
        }
        this.current.addChild(rangeLeaf);
        return this;
    }

    public QueryBuilder endStatement() {
        checkFinished();
        finished = true;
        if (parentBuilder == null)
            return this;
        return parentBuilder;
    }
}
