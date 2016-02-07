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
package org.calrissian.mango.criteria.domain;

import java.util.Objects;

public class RangeLeaf<T> extends TypedTermLeaf<T> {

    private final T start;
    private final T end;

    public RangeLeaf(String term, T start, T end, ParentNode parent) {
        super(term, firstKnownType(start, end), parent);
        this.start = start;
        this.end = end;
    }

    public Object getStart() {
        return start;
    }

    public Object getEnd() {
        return end;
    }

    @Override
    public Node clone(ParentNode node) {
        return new RangeLeaf<>(getTerm(), getStart(), getEnd(), node);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        RangeLeaf<?> rangeLeaf = (RangeLeaf<?>) o;
        return Objects.equals(start, rangeLeaf.start) &&
                Objects.equals(end, rangeLeaf.end);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), start, end);
    }

    @Override
    public String toString() {
        return getTerm() + " within " +
                (start == null ? "(-inf" : "[" + start) +
                "," +
                (end == null ? "inf)" : end + "]");
    }
}
