package org.calrissian.mango.collect;

import org.junit.Test;

import java.io.IOException;

public class AbstractCloseableIteratorTest {

    @Test(expected = UnsupportedOperationException.class)
    public void removeThrowsUnsupportedOperationException() {
        AbstractCloseableIterator iterator = testIterator();
        iterator.remove();
    }

    private <T> AbstractCloseableIterator<T> testIterator() {
        return new AbstractCloseableIterator<T>() {
            @Override
            protected T computeNext() {
                throw new RuntimeException();
            }

            @Override
            public void close() throws IOException {
                //do nothing.
            }
        };
    }
}
