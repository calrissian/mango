/*
 * Copyright (C) 2013 The Calrissian Authors
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

import com.google.common.base.Joiner;
import org.calrissian.mango.domain.AttributeStore;

public class AndCriteria extends ParentCriteria {

    public AndCriteria(ParentCriteria parent) {
        super(parent);
    }

    @Override
    public boolean apply(AttributeStore obj) {
        for (Criteria node : children()) {
            if (!node.apply(obj))
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
        return "(" + Joiner.on(" or ").join(children()) + ")";
    }
}
