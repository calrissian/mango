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

import com.google.common.collect.ImmutableList;

import java.util.*;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.collect.Iterables.concat;

/**
 * A base tuple collection providing reusable implementations for interacting with a tuple store backed by
 * a hash map with sets in the value representing a multimap.
 */
public class BaseTupleStore implements TupleStore {

    private Map<String, List<Tuple>> tuples = new HashMap<String, List<Tuple>>();

    public void put(Tuple tuple) {
        checkNotNull(tuple);
        checkNotNull(tuple.getKey());

        List<Tuple> keyedTuples = tuples.get(tuple.getKey());
        if (keyedTuples == null) {
            keyedTuples = new ArrayList<Tuple>();
            tuples.put(tuple.getKey(), keyedTuples);
        }
        keyedTuples.add(tuple);
    }

    public void putAll(Iterable<Tuple> tuples) {
        checkNotNull(tuples);
        for (Tuple tuple : tuples)
            put(tuple);
    }


    /**
     * Returns all the getTuples set on the current entity
     */
    public Collection<Tuple> getTuples() {
        return ImmutableList.copyOf(concat(tuples.values()));
    }

    /**
     * A get operation for multi-valued keys
     */
    public Collection<Tuple> getAll(String key) {
        checkNotNull(key);
        return tuples.get(key);
    }

    /**
     * A get operation for single-valued keys
     */
    public <T> Tuple<T> get(String key) {
        return tuples.get(key) != null ? tuples.get(key).iterator().next() : null;
    }

    @Override
    public Set<String> keys() {
        return tuples.keySet();
    }

    @Override
    public boolean containsKey(String key) {
        return tuples.containsKey(key);
    }

    @Override
    public <T> Tuple<T> remove(Tuple<T> t) {
        checkNotNull(t);
        checkNotNull(t.getKey());
        if (tuples.containsKey(t.getKey())) {
            List<Tuple> tupelSet = tuples.get(t.getKey());
            if(tupelSet.remove(t))
                return t;
        }
        return null;
    }

    @Override
    public <T> Tuple<T> remove(String key) {
        checkNotNull(key);
        if (tuples.containsKey(key)) {
            List<Tuple> tupleSet = tuples.get(key);
            Tuple t = tupleSet.size() > 0 ? tupleSet.iterator().next() : null;
            if(t != null && tuples.get(key).remove(t))
                return t;
        }

        return null;
    }

    @Override
    public Collection<Tuple> removeAll(String key){
        checkNotNull(key);
        return tuples.remove(key);
    }


    @Override
    public Collection<Tuple> removeAll(Collection<Tuple> tuples) {
        checkNotNull(tuples);
        Collection<Tuple> removedTuples = new LinkedList<Tuple>();
        for (Tuple tuple : tuples)
            removedTuples.add(remove(tuple));
        return removedTuples;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BaseTupleStore)) return false;

        BaseTupleStore that = (BaseTupleStore) o;

        if (!tuples.equals(that.tuples)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return tuples.hashCode();
    }
}
