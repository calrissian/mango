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
package org.calrissian.mango.criteria.domain.criteria;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static java.util.Collections.unmodifiableList;

public abstract class ParentCriteria implements Criteria {
    private static final long serialVersionUID = 1L;

    private final List<Criteria> nodes;
    private final ParentCriteria parent;

    public ParentCriteria() {
        this(null);
    }

    public ParentCriteria(ParentCriteria parent) {
        this(parent, new ArrayList<Criteria>());
    }

    public ParentCriteria(ParentCriteria parent, List<Criteria> nodes) {
        this.parent = parent;
        this.nodes = nodes;
    }

    @Override
    public ParentCriteria parent() {
        return parent;
    }

    @Override
    public List<Criteria> children() {
        return unmodifiableList(nodes);
    }

    @Override
    public void addChild(Criteria node) {
        nodes.add(node);
    }

    @Override
    public void removeChild(Criteria node) {
        nodes.remove(node);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ParentCriteria that = (ParentCriteria) o;
        //Don't include parent in equals check.
        return Objects.equals(nodes, that.nodes);
    }

    @Override
    public int hashCode() {
        //Don't include parent in hashcode.
        return Objects.hash(nodes);
    }

    @Override
    public String toString() {
        return "ParentCriteria{" +
                "nodes=" + nodes +
                ", parent=" + parent +
                '}';
    }
}
