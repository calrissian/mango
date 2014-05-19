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

import com.google.common.base.Predicate;
import org.calrissian.mango.domain.TupleStore;

import java.io.Serializable;
import java.util.List;

/**
 * Criteria represents a predicate tree which can applied to {@link org.calrissian.mango.domain.TupleStore} objects. Custom predicates can
 * also be plugged in to the tree and evaluated.
 */
public interface Criteria extends Predicate<TupleStore>, Serializable {

    boolean apply(TupleStore obj);

    public ParentCriteria parent();

    public List<Criteria> children();

    public void addChild(Criteria node);

    public void removeChild(Criteria node);
}
