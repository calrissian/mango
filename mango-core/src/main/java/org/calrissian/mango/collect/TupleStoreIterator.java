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
package org.calrissian.mango.collect;

import com.google.common.collect.PeekingIterator;
import org.calrissian.mango.domain.Tuple;
import org.calrissian.mango.domain.TupleStore;

import java.util.Iterator;

import static com.google.common.collect.Iterators.peekingIterator;

public class TupleStoreIterator<T extends TupleStore> implements Iterator<Tuple> {

    private PeekingIterator<T> itr = null;
    private T curTupleCollection = null;
    private Iterator<Tuple> tuples = null;

    public TupleStoreIterator(Iterable<T> tupleCollections) {
        this.itr = peekingIterator(tupleCollections.iterator());
    }

    @Override
    public boolean hasNext() {
        if (tuples != null && tuples.hasNext())
            return true;
        else if (itr.hasNext()) {
            return itr.peek().getTuples().size() > 0;
        }
        return false;
    }

    @Override
    public Tuple next() {

        Tuple next = null;
        if (tuples != null && tuples.hasNext())
            return tuples.next();
        else if (itr.hasNext()) {
            while (tuples == null || !tuples.hasNext()) {
                curTupleCollection = itr.next();
                tuples = curTupleCollection.getTuples().iterator();
            }
            if (tuples.hasNext()) {
                next = tuples.next();
            } else
                throw new RuntimeException("Iteration interrupted");
        }

        return next;
    }

    @Override
    public void remove() {

    }

    public T getTopStore() {
        return curTupleCollection;
    }
}