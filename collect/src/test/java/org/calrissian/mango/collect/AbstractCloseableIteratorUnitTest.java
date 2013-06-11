package org.calrissian.mango.collect;

import org.junit.Test;

import java.io.IOException;

public class AbstractCloseableIteratorUnitTest {

    @Test(expected = UnsupportedOperationException.class)
    public void removeThrowsUnsupportedOperationException() {
        AbstractCloseableIterator iterator = createMockIterator();
        iterator.remove();
    }


    private <T> AbstractCloseableIterator<T> createMockIterator() {
        return new MockAbstractCloseableIterator<T>();
    }

    class MockAbstractCloseableIterator<T> extends AbstractCloseableIterator<T> {

        @Override
        protected T computeNext() {
            throw new RuntimeException();
        }

        @Override
        public void close() throws IOException {
        }
    }
}
