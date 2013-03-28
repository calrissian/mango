package org.calrissian.mango.collect;

import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertEquals;

public class AbstractCloseableIteratorUnitTest {

    @Test(expected = UnsupportedOperationException.class)
    public void removeThrowsUnsupportedOperationException() {
        org.calrissian.mango.collect.AbstractCloseableIterator iterator = createMockIterator();
        iterator.remove();
    }

    @Test
    public void iteratorReturnsThis() {
        AbstractCloseableIterator iterator = createMockIterator();
        assertEquals(iterator, iterator.iterator());
    }



    private <T> AbstractCloseableIterator<T> createMockIterator() {
        return new MockAbstractCloseableIterator<T>();
    }

    class MockAbstractCloseableIterator<T> extends AbstractCloseableIterator<T> {

        @Override
        public T next() {
            throw new RuntimeException();
        }

        @Override
        public void close() throws IOException {
        }

        @Override
        public boolean hasNext() {
            throw new RuntimeException();
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }
    }
}
