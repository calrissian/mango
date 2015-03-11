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
package org.calrissian.mango.criteria.domain.criteria;

import org.calrissian.mango.domain.Attribute;
import org.calrissian.mango.domain.AttributeStore;

import java.util.Collection;
import java.util.Comparator;

public class GreaterThanCriteria extends ComparableKeyValueLeafCriteria {
    public GreaterThanCriteria(String key, Object value, Comparator comparator, ParentCriteria parentCriteria) {
        super(key, value, comparator, parentCriteria);
    }

    @Override
    public Criteria clone(ParentCriteria parentCriteria) {
        return new GreaterThanCriteria(key, value, comparator, parentCriteria);
    }


    @Override
    public boolean apply(AttributeStore obj) {
        Collection<Attribute> attributes = obj.getAll(key);
        if (attributes != null) {
            for (Attribute attribute : attributes) {
                return comparator.compare(attribute.getValue(), value) > 0;
            }
        }

        return false;
    }
}
