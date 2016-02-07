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

public class RangeLeaf extends AbstractKeyValueLeaf {
    private static final long serialVersionUID = 1L;

    protected String key;
    protected Object end;

    public RangeLeaf(String key, Object start, Object end, ParentNode parent) {
        super(key, start, parent);
        this.key = key;
        this.end = end;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Object getStart() {
        return getValue();
    }

    public void setStart(Object start) {
        this.value = start;
    }

    public Object getEnd() {
        return end;
    }

    public void setEnd(Object end) {
        this.end = end;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        RangeLeaf rangeLeaf = (RangeLeaf) o;

        if (end != null ? !end.equals(rangeLeaf.end) : rangeLeaf.end != null) return false;
        if (key != null ? !key.equals(rangeLeaf.key) : rangeLeaf.key != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (key != null ? key.hashCode() : 0);
        result = 31 * result + (end != null ? end.hashCode() : 0);
        return result;
    }


    @Override
    public Node clone(ParentNode node) {
        return new RangeLeaf(key, value, end, node);
    }
}
