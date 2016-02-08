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

import java.util.List;

public abstract class LeafCriteria implements Criteria {

    private final ParentCriteria parent;

    public LeafCriteria(ParentCriteria parentCriteria) {
        this.parent = parentCriteria;
    }

    @Override
    public ParentCriteria parent() {
        return parent;
    }

    @Override
    public List<Criteria> children() {
        return null;
    }

    @Override
    public void addChild(Criteria node) {
        throw new UnsupportedOperationException("Leaf does not have children");
    }


    @Override
    public void removeChild(Criteria node) {
        throw new UnsupportedOperationException("Leaf does not have children");
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        //Don't include parent in equals check.
        return true;
    }

    @Override
    public int hashCode() {
        //Don't include parent in hashcode.
        return 0;
    }
}
