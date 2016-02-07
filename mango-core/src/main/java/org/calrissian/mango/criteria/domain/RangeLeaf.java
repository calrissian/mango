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
package org.calrissian.mango.criteria.domain;

import java.util.Objects;

public class RangeLeaf extends Leaf {
    private static final long serialVersionUID = 1L;

    protected String key;
    protected Object start;
    protected Object end;

    public RangeLeaf(String key, Object start, Object end, ParentNode parent) {
        super(parent);
        this.key = key;
        this.start = start;
        this.end = end;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Object getStart() {
        return start;
    }

    public void setStart(Object start) {
        this.start = start;
    }

    public Object getEnd() {
        return end;
    }

    public void setEnd(Object end) {
        this.end = end;
    }

    @Override
    public Node clone(ParentNode node) {
        return new RangeLeaf(key, start, end, node);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof RangeLeaf)) return false;

        RangeLeaf rangeLeaf = (RangeLeaf) o;
        return Objects.equals(key, rangeLeaf.key) &&
                Objects.equals(start, rangeLeaf.start) &&
                Objects.equals(end, rangeLeaf.end);
    }

    @Override
    public int hashCode() {
        return Objects.hash(key, start, end);
    }
}
