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

public class RangeCriteria extends ComparableKeyValueLeafCriteria {

    protected Object end;
    protected String encodedEnd;

    public RangeCriteria(String key, Object start, Object end, Comparator comparator, ParentCriteria parentCriteria) {
        super(key, start, comparator, parentCriteria);
        this.end = end;
    }


    @Override
    public Criteria clone(ParentCriteria parentCriteria) {
        return new RangeCriteria(key, value, end, comparator, parentCriteria);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        RangeCriteria that = (RangeCriteria) o;

        if (encodedEnd != null ? !encodedEnd.equals(that.encodedEnd) : that.encodedEnd != null) return false;
        if (end != null ? !end.equals(that.end) : that.end != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (end != null ? end.hashCode() : 0);
        result = 31 * result + (encodedEnd != null ? encodedEnd.hashCode() : 0);
        return result;
    }

    @Override
    public boolean apply(AttributeStore obj) {
        Collection<Attribute> attributes = obj.getAll(key);
        if (attributes != null) {
            for (Attribute attribute : attributes) {
                int startCompare = comparator.compare(attribute.getValue(), value);
                int endCompare = comparator.compare(attribute.getValue(), end);
                if (startCompare >= 0 && endCompare <= 0) {
                    return true;
                }
            }
        }

        return false;
    }
}
