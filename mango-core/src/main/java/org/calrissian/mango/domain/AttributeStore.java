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
package org.calrissian.mango.domain;

import java.io.Serializable;
import java.util.Collection;
import java.util.Set;

public interface AttributeStore extends Serializable {

    /**
     * Puts a single keyValue in the current data store
     */
    void put(Attribute keyValue);

    /**
     * Adds all the given getTuples to the current data store
     */
    void putAll(Iterable<? extends Attribute> attributes);

    /**
     * Retrieves all the getTuples.
     */
    Collection<? extends Attribute> getTuples();

    /**
     * Retrieves all the attributes for the specified key.
     */
    Collection<? extends Attribute> getAll(String key);

    /**
     * Retrieves the first attribute returned for the specified key. This method assumes a single-valued key.
     * Note that multi-vaued keys may give undeterministic results.
     */
    <T> Attribute<T> get(String key);

    /**
     * Returns the keys in the current object
     */
    Set<String> keys();

    /**
     * Returns true of there exist attributes with the specified key
     */
    boolean containsKey(String key);

    /**
     * Removes the specified attribute
     */
    <T> Attribute<T> remove(Attribute<T> t);

    /**
     * Removes the first attribute belonging to the specified key. This method assumed single-valued key
     */
    <T> Attribute<T> remove(String key);

    /**
     * Revoves all the attributes with the given key.
     */
    Collection<? extends Attribute> removeAll(String key);

    Collection<? extends Attribute> removeAll(Collection<? extends Attribute> keyValues);

    /**
     * Returns the size of the attributestore. This should be a constant-time operation
     * @return
     */
    int size();
}
