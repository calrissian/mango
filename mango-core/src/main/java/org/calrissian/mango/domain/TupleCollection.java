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

public interface TupleCollection extends Serializable{

  /**
   * Puts a single tuple in the current collection
   */
  void put(Tuple tuple);

  /**
   * Adds all the given getTuples to the current collection
   */
  void putAll(Iterable<Tuple> tuples);

  /**
   * Retrieves all the getTuples.
   */
  Collection<Tuple> getTuples();

  /**
   * Retrieves all the getTuples for the specified key.
   */
  Collection<Tuple> getAll(String key);

  /**
   * Retrieves the first tuple returned for the specified key. This method assumes a single-valued key.
   * Note that multi-vaued keys may give undeterministic results.
   */
  <T>Tuple<T> get(String key);

  /**
   * Returns the keys in the current object
   */
  Set<String> keys();

  /**
   * Removes the specified tuple
   */
  <T>Tuple<T> remove(Tuple<T> t);

  /**
   * Removes the first tuple belonging to the specified key. This method assumed single-valued key
   */
  <T>Tuple<T> remove(String key);

  Collection<Tuple> removeAll(String key);
}