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

import org.calrissian.mango.domain.Attribute;
import org.calrissian.mango.domain.AttributeStore;

import java.util.Comparator;
import java.util.Objects;

import static com.google.common.base.Preconditions.checkNotNull;

public class RangeCriteria<T> extends TermCriteria {

    private final T start;
    private final T end;
    private final Comparator<T> comparator;

    public RangeCriteria(String term, T start, T end, Comparator<T> comparator, ParentCriteria parentCriteria) {
        super(term, parentCriteria);
        this.start = start;
        this.end = end;
        this.comparator = checkNotNull(comparator);
    }

    @Override
    public boolean apply(AttributeStore obj) {
        for (Attribute attribute : obj.getAttributes(getTerm())) {
            if (comparator.compare((T)(attribute.getValue()), start) >= 0
                    && comparator.compare((T)(attribute.getValue()), end) <= 0) {
                return true;
            }
        }

        return false;
    }

    @Override
    public Criteria clone(ParentCriteria parentCriteria) {
        return new RangeCriteria<>(getTerm(), start, end, comparator, parentCriteria);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        RangeCriteria<?> that = (RangeCriteria<?>) o;
        return Objects.equals(start, that.start) &&
                Objects.equals(end, that.end) &&
                Objects.equals(comparator, that.comparator);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), start, end, comparator);
    }

    @Override
    public String toString() {
        return getTerm() + " within " +
                (start == null ? "(-inf" : "[" + start) +
                "," +
                (end == null ? "inf)" : end + "]");
    }
}
