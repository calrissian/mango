/*
 * Copyright (C) 2017 The Calrissian Authors
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

public class HasLeaf<T> extends TypedTermLeaf<T> {

    public HasLeaf(String term, Class<T> clazz, ParentNode parent) {
        super(term, clazz, parent);
    }

    public HasLeaf(String term, ParentNode parent) {
        this(term, null, parent);
    }

    @Override
    public Node clone(ParentNode node) {
        return new HasLeaf<>(getTerm(), getType(), node);
    }

    @Override
    public String toString() {
        return "hasTerm('" + getTerm() + "')";
    }
}
