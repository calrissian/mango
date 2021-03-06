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
package org.calrissian.mango.criteria.domain;

import java.util.Objects;

import static java.util.Objects.requireNonNull;

public abstract class TermLeaf extends Leaf {

    private final String term;

    public TermLeaf(String term, ParentNode parent) {
        super(parent);
        this.term = requireNonNull(term);
    }

    public String getTerm() {
        return term;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        TermLeaf termLeaf = (TermLeaf) o;
        return Objects.equals(term, termLeaf.term);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), term);
    }
}
