/*
 * Copyright (C) 2019 The Calrissian Authors
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

import org.calrissian.mango.domain.AttributeStore;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static java.util.stream.Collectors.joining;

public class AndCriteria extends ParentCriteria {

    public AndCriteria(ParentCriteria parent) {
        this(new ArrayList<>(), parent);
    }

    public AndCriteria(List<Criteria> nodes, ParentCriteria parent) {
        super(nodes, parent);
    }

    @Override
    public boolean test(AttributeStore obj) {
        for (Criteria node : children()) {
            if (!node.test(obj))
                return false;
        }

        return true;
    }

    @Override
    public Criteria clone(ParentCriteria node) {
        AndCriteria cloned = new AndCriteria(node);
        for (Criteria child : children())
            cloned.addChild(child.clone(cloned));
        return cloned;
    }

    @Override
    public String toString() {
        return "(" + children().stream().map(Objects::toString).collect(joining(" and ")) + ")";
    }
}
