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
package org.calrissian.mango.domain;


import java.io.Serializable;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static com.google.common.base.Preconditions.checkNotNull;
import static java.util.Collections.unmodifiableMap;
import static java.util.Collections.unmodifiableSet;

/**
 * A value class representing a key/value pair with metadata. This class is immutable.
 */
public class Attribute<T> implements Serializable {

    protected final String key;
    protected final T value;

    /**
     * Metadata allows the attribute to be extensible so that different services can read different properties without
     * the need for inheritance.
     */
    protected final Map<String,String> metadata;

    public Attribute(String key, T value) {
        this(key, value, Collections.<String,String>emptyMap());
    }

    public Attribute(String key, T value, Map<String, String> metadata) {
        checkNotNull(key);
        checkNotNull(value);
        checkNotNull(metadata);

        this.key = key;
        this.value = value;
        this.metadata = new HashMap<>(metadata);
    }

    public String getKey() {
        return key;
    }

    public T getValue() {
        return value;
    }

    /**
     * Gets all keys from the metadata
     */
    public Set<String> metadataKeys() {
        return unmodifiableSet(metadata.keySet());
    }

    /**
     * Gets a value for the metadata key for the current attribute.
     */
    public String getMetadataValue(String key) {
        return metadata.get(key);
    }

    /**
     * Gets an immutable view of the current metadata
     */
    public Map<String,String> getMetadata() {
        return unmodifiableMap(metadata);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Attribute)) return false;

        Attribute attribute = (Attribute) o;

        if (!key.equals(attribute.key)) return false;
        if (!metadata.equals(attribute.metadata)) return false;
        if (!value.equals(attribute.value)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = key.hashCode();
        result = 31 * result + value.hashCode();
        result = 31 * result + metadata.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "Attribute{" +
                "key='" + key + '\'' +
                ", value=" + value +
                ", metadata=" + metadata +
                '}';
    }
}
