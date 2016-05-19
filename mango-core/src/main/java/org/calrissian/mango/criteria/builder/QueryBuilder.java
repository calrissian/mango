/*
 * Copyright (C) 2016 The Calrissian Authors
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
package org.calrissian.mango.criteria.builder;

import org.calrissian.mango.criteria.domain.*;

import java.util.Collection;

import static java.util.Arrays.asList;

public class QueryBuilder {
    protected ParentNode current;
    protected QueryBuilder parentBuilder;
    protected boolean finished = false;

    public static QueryBuilder create() {
        return new QueryBuilder();
    }

    protected QueryBuilder() {}


    protected QueryBuilder(ParentNode current, QueryBuilder parentBuilder) {
        this.current = current;
        this.parentBuilder = parentBuilder;
    }

    public QueryBuilder and() {
        checkFinished();
        AndNode andNode = new AndNode(current);
        if (current != null) {
            current.addChild(andNode);
            return new QueryBuilder(andNode, this);
        }
        current = andNode;
        return new QueryBuilder(andNode, null);
    }

    public QueryBuilder or() {
        checkFinished();
        OrNode orNode = new OrNode(current);
        if (current != null) {
            current.addChild(orNode);
            return new QueryBuilder(orNode, this);
        }
        current = orNode;
        return new QueryBuilder(orNode, null);
    }

    public Node build() {
        if (!finished) throw new IllegalArgumentException("Query Node not built, end first");
        return current;
    }

    public QueryBuilder eq(String type, Object value) {
        checkFinished();
        if (this.current == null) {
            this.current = new AndNode();
            finished = true;
        }
        EqualsLeaf equalsLeaf = new EqualsLeaf<>(type, value, current);
        this.current.addChild(equalsLeaf);
        return this;
    }

    protected void checkFinished() {
        if (finished)
            throw new IllegalArgumentException("Builder finished. Call build() to get constructed Query Node");
    }

    public QueryBuilder has(String key) {
        checkFinished();
        if (this.current == null) {
            this.current = new AndNode();
            finished = true;
        }
        HasLeaf hasKeyLeaf = new HasLeaf(key, current);
        this.current.addChild(hasKeyLeaf);
        return this;
    }

    public QueryBuilder hasNot(String key) {
        checkFinished();
        if (this.current == null) {
            this.current = new AndNode();
            finished = true;
        }
        HasNotLeaf hasNotLeaf = new HasNotLeaf(key, current);
        this.current.addChild(hasNotLeaf);
        return this;
    }

    public QueryBuilder in(String key, Collection<Object> values) {
        checkFinished();
        if(this.current == null) {
            this.current = new AndNode();
            finished = true;
        }
        InLeaf leaf = new InLeaf<>(key, values, current);
        this.current.addChild(leaf);
        return this;
    }

    public QueryBuilder in(String key, Object... values) {
        return in(key, asList(values));
    }

    public QueryBuilder notIn(String key, Collection<Object> values) {
        checkFinished();
        if(this.current == null) {
            this.current = new AndNode();
            finished = true;
        }
        NotInLeaf leaf = new NotInLeaf<>(key, values, current);
        this.current.addChild(leaf);
        return this;
    }

    public QueryBuilder notIn(String key, Object... values) {
        return notIn(key, asList(values));
    }


    public QueryBuilder notEq(String type, Object value) {
        checkFinished();
        if (this.current == null) {
            this.current = new AndNode();
            finished = true;
        }
        NotEqualsLeaf notEqualsLeaf = new NotEqualsLeaf<>(type, value, current);
        this.current.addChild(notEqualsLeaf);
        return this;
    }


    public QueryBuilder lessThan(String type, Object value) {
        checkFinished();
        if (this.current == null) {
            this.current = new AndNode();
            finished = true;
        }
        LessThanLeaf leaf = new LessThanLeaf<>(type, value, current);
        this.current.addChild(leaf);
        return this;
    }

    public QueryBuilder lessThanEq(String type, Object value) {
        checkFinished();
        if (this.current == null) {
            this.current = new AndNode();
            finished = true;
        }
        LessThanEqualsLeaf leaf = new LessThanEqualsLeaf<>(type, value, current);
        this.current.addChild(leaf);
        return this;
    }

    public QueryBuilder greaterThan(String type, Object value) {
        checkFinished();
        if (this.current == null) {
            this.current = new AndNode();
            finished = true;
        }
        GreaterThanLeaf leaf = new GreaterThanLeaf<>(type, value, current);
        this.current.addChild(leaf);
        return this;
    }

    public QueryBuilder greaterThanEq(String type, Object value) {
        checkFinished();
        if (this.current == null) {
            this.current = new AndNode();
            finished = true;
        }
        GreaterThanEqualsLeaf leaf = new GreaterThanEqualsLeaf<>(type, value, current);
        this.current.addChild(leaf);
        return this;
    }


    public QueryBuilder range(String type, Object start, Object end) {
        checkFinished();
        if (this.current == null) {
            this.current = new AndNode();
            finished = true;
        }
        RangeLeaf rangeLeaf = new RangeLeaf<>(type, start, end, current);
        this.current.addChild(rangeLeaf);
        return this;
    }


    public QueryBuilder end() {
        checkFinished();
        finished = true;
        if (parentBuilder == null)
            return this;
        return parentBuilder;
    }
}
