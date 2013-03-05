package com.texeltek.mango.collect;

import com.google.common.collect.Iterables;
import com.google.common.collect.Iterators;
import com.google.common.collect.Lists;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;

/**
 */
public class CloseableIteratorsTest {
    @Test
    public void testSortedDistinct() throws Exception {
        CloseableIterator<Integer> integers = mockCloseableIterator(Lists.newArrayList(1, 1, 2, 2, 3, 3, 3, 4, 5, 6, 7, 7, 7));
        CloseableIterator<Integer> distinct = CloseableIterators.sortedDistinct(integers);
        assertEquals(7, Iterators.size(distinct));
    }

    private <T> CloseableIterator<T> mockCloseableIterator(ArrayList<T> integers) {
        return CloseableIterators.wrap(integers.iterator());
    }
}
