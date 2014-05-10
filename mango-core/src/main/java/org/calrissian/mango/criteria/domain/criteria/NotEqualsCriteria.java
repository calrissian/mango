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
package org.calrissian.mango.criteria.domain.criteria;

import org.calrissian.mango.domain.Tuple;
import org.calrissian.mango.domain.TupleCollection;

import java.util.Collection;

public class NotEqualsCriteria extends AbstractKeyValueLeafCriteria {

  public NotEqualsCriteria(String key, Object value, ParentCriteria parentCriteria) {
    super(key, value, parentCriteria);
  }

  @Override
  public boolean matches(TupleCollection obj) {
    Collection<Tuple> tuples = obj.getAll(key);
    if(tuples != null) {
      for(Tuple tuple : tuples) {
        if(tuple.getValue() != null && tuple.getValue().equals(value))
          return false;
      }
    }

    return true;
  }
}
