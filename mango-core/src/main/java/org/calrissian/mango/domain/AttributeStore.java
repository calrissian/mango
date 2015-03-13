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
     * Retrieves all the getAttributes.
     */
    Collection<Attribute> getAttributes();

    /**
     * Retrieves all the attributes for the specified key.
     */
    Collection<Attribute> getAll(String key);

    /**
     * Retrieves the first attribute returned for the specified key. This method assumes a single-valued key.
     * Note that multi-vaued keys may give undeterministic results.
     */
    <T> Attribute<T> get(String key);

    <T> Attribute<T> get(String key, Class<T> clazz);


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
    Collection<Attribute> removeAll(String key);

    Collection<Attribute> removeAll(Collection<Attribute> attributes);

    /**
     * Returns the size of the attributestore. This should be a constant-time operation
     * @return
     */
    int size();
}
