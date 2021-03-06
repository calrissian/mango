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
package org.calrissian.mango.domain;

import com.google.common.collect.ArrayListMultimap;

import java.util.Collection;
import java.util.Set;

import static com.google.common.collect.Iterables.getFirst;
import static java.util.Collections.unmodifiableCollection;
import static java.util.Collections.unmodifiableSet;
import static java.util.Objects.requireNonNull;

/**
 * A base attribute collection providing reusable implementations for interacting with a attribute store backed by
 * a hash map with sets in the value representing a multimap.
 */
public abstract class AbstractAttributeStore implements AttributeStore {

    private static final long serialVersionUID = 1L;

    private final ArrayListMultimap<String, Attribute> attributes;

    protected AbstractAttributeStore(Iterable<? extends Attribute> attributes) {
        requireNonNull(attributes);
        this.attributes = ArrayListMultimap.create();
        for (Attribute attr : attributes) {
            this.attributes.put(attr.getKey(), attr);
        }
    }

    @Override
    public Collection<Attribute> getAttributes() {
        return unmodifiableCollection(attributes.values());
    }

    @Override
    public Collection<Attribute> getAttributes(String key) {
        return unmodifiableCollection(attributes.get(key));
    }

    @Override
    public <T> Attribute<T> get(String key) {
        return getFirst(getAttributes(key), null);
    }

    @Override
    public Set<String> keys() {
        return unmodifiableSet(attributes.keySet());
    }

    @Override
    public boolean contains(String key) {
        return attributes.containsKey(key);
    }

    @Override
    public int size() {
        return attributes.size();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AbstractAttributeStore)) return false;

        AbstractAttributeStore that = (AbstractAttributeStore) o;

        return attributes.equals(that.attributes);

    }

    @Override
    public int hashCode() {
        return attributes.hashCode();
    }
}
