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
package org.calrissian.mango.criteria.domain;

public class HasNotLeaf<T> extends TypedTermLeaf<T> implements NegationLeaf {

    public HasNotLeaf(String term, Class<T> clazz, ParentNode parent) {
        super(term, clazz, parent);
    }

    public HasNotLeaf(String term, ParentNode parentNode) {
        this(term, null, parentNode);
    }

    @Override
    public Node clone(ParentNode node) {
        return new HasNotLeaf<>(getTerm(), getType(), node);
    }
}
