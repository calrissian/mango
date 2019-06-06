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

import org.calrissian.mango.domain.Attribute;
import org.calrissian.mango.domain.AttributeStore;

import java.util.Comparator;

public class LessThanEqualsCriteria<T> extends ComparableTermValueCriteria<T> {

    public LessThanEqualsCriteria(String term, T value, Comparator<T> comparator, ParentCriteria parentCriteria) {
        super(term, value, comparator, parentCriteria);
    }

    @Override
    public boolean test(AttributeStore obj) {
        for (Attribute attribute : obj.getAttributes(getTerm())) {
            if (getComparator().compare((T)(attribute.getValue()), getValue()) <= 0)
                return true;
        }
        return false;
    }

    @Override
    public Criteria clone(ParentCriteria parentCriteria) {
        return new LessThanEqualsCriteria<>(getTerm(), getValue(), getComparator(), parentCriteria);
    }

    @Override
    public String toString() {
        return getTerm() + " <= " + getValue();
    }
}
