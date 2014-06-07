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


import com.google.common.collect.Maps;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * A value class representing a key/value pair with a visiblity. This class is immutable.
 */
public class Tuple<T> implements Serializable {

    protected final String key;
    protected final T value;

    /**
     * Metadata allows the tuple to be extensible so that different services can read different properties without
     * the need for inheritance.
     */
    protected final Map<String,Object> metadata = new HashMap<String, Object>();

    public Tuple(String key, T value) {
        checkNotNull(key);
        checkNotNull(value);
        this.key = key;
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public T getValue() {
        return value;
    }

    /**
     * Sets a key/value pair on the metadata for the current tuple.
     */
    public void setMetadataValue(String key, Object value) {
        metadata.put(key, value);
    }

    /**
     * Gets a key/value pair from the metadata for the current tuple.
     */
    public <T>T getMetadataValue(String key) {
        return (T)metadata.get(key);
    }

    public Set<String> metadataKeys() {
        return metadata.keySet();
    }

    /**
     * Gets a copy of the current metadata
     */
    public Map<String,Object> getMetadata() {
        return Maps.newHashMap(metadata);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Tuple tuple = (Tuple) o;

        if (key != null ? !key.equals(tuple.key) : tuple.key != null) return false;
        if (metadata != null ? !metadata.equals(tuple.metadata) : tuple.metadata != null) return false;
        if (value != null ? !value.equals(tuple.value) : tuple.value != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = key != null ? key.hashCode() : 0;
        result = 31 * result + (value != null ? value.hashCode() : 0);
        result = 31 * result + (metadata != null ? metadata.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Tuple{" +
                "key='" + key + '\'' +
                ", value=" + value +
                ", metadata=" + metadata +
                '}';
    }
}
