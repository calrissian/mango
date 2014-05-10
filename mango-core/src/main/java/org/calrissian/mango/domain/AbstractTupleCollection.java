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

import java.util.*;

import static com.google.common.collect.Iterables.addAll;
import static com.google.common.collect.Iterables.concat;
import static java.util.Collections.unmodifiableCollection;

public abstract class AbstractTupleCollection implements TupleCollection{

  private Map<String,Set<Tuple>> tuples = new HashMap<String, Set<Tuple>>();

  public void put(Tuple tuple) {
    Set<Tuple> keyedTuples = tuples.get(tuple.getKey());
    if(keyedTuples == null) {
      keyedTuples = new HashSet<Tuple>();
      tuples.put(tuple.getKey(), keyedTuples);
    }
    keyedTuples.add(tuple);
  }

  public void putAll(Iterable<Tuple> tuples) {
    for(Tuple tuple : tuples)
      put(tuple);
  }


  /**
   * Returns all the getTuples set on the current entity
   */
  public Collection<Tuple> getTuples() {
    Collection<Tuple> tupleCollection = new LinkedList<Tuple>();
    addAll(tupleCollection, concat(tuples.values()));
    return unmodifiableCollection(tupleCollection);
  }

  /**
   * A get operation for multi-valued keys
   */
  public Collection<Tuple> getAll(String key) {
    return tuples.get(key);
  }

  /**
   * A get operation for single-valued keys
   */
  public <T>Tuple<T> get(String key) {
    return tuples.get(key) != null ? tuples.get(key).iterator().next() : null;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    AbstractTupleCollection that = (AbstractTupleCollection) o;

    if (tuples != null ? !tuples.equals(that.tuples) : that.tuples != null) return false;

    return true;
  }

  @Override
  public Set<String> keys() {
    return tuples.keySet();
  }

  @Override
  public <T> Tuple<T> remove(Tuple<T> t) {
    if(tuples.containsKey(t.getKey())) {
      Set<Tuple> tupleForKey = tuples.get(t.getKey());
      Tuple<T> tuple = (Tuple<T>)tuples.remove(t);
      return tuple;
    }

    return null;
  }

  @Override
  public Collection<Tuple> remove(String key) {
    return tuples.remove(key);
  }

  @Override
  public int hashCode() {
    return tuples != null ? tuples.hashCode() : 0;
  }
}
