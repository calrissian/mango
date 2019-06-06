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

public abstract class TypedTermLeaf<T> extends TermLeaf {

    //Utility method to allow subclasses to extract type from known variables.
    protected static <T> Class<T> firstKnownType(T... objects) {
        for (T obj : objects)
            if (obj != null)
                return (Class<T>) obj.getClass();
        return null;
    }

    private final Class<T> type;

    public TypedTermLeaf(String term, Class<T> type, ParentNode parent) {
        super(term, parent);
        this.type = type;
    }

    public Class<T> getType() {
        return type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        TypedTermLeaf<?> that = (TypedTermLeaf<?>) o;
        return Objects.equals(type, that.type);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), type);
    }
}
