package org.calrissian.mango.domain;


import com.google.common.collect.Iterables;
import org.junit.Before;
import org.junit.Test;

import java.util.Collection;

import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;

public class BaseTupleCollectionTest {

    Tuple tuple1 = new Tuple("key1", "val1");
    Tuple tuple2 = new Tuple("key1", "val2");
    Tuple tuple3 = new Tuple("key2", "val2");
    Tuple tuple4 = new Tuple("key3", "val3");

    BaseTupleCollection tupleCollection = new BaseTupleCollection();

    @Before
    public void setup() {
        tupleCollection.putAll(asList(new Tuple[]{tuple1, tuple2, tuple3, tuple4}));
    }

    @Test
    public void testRemoveAll_forManyTuples() {

        Collection<Tuple> removed = tupleCollection.removeAll(asList(new Tuple[]{tuple2, tuple3}));
        assertEquals(2, removed.size());
        assertEquals(tuple2, Iterables.get(removed, 0));
        assertEquals(tuple3, Iterables.get(removed, 1));
        assertEquals(1, tupleCollection.getAll(tuple2.getKey()).size());
        assertEquals(0, tupleCollection.getAll(tuple3.getKey()).size());
    }


    @Test
    public void testRemoveAll_singleTuple() {
        Tuple removed = tupleCollection.remove(tuple1);
        assertEquals(tuple1, removed);
        assertEquals(1, tupleCollection.getAll(tuple1.getKey()).size());
    }
}
