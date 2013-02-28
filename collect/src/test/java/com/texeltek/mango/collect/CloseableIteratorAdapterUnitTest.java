package com.texeltek.mango.collect;

import org.junit.Test;
import org.mockito.Mockito;

import java.io.IOException;
import java.util.Iterator;

import static org.junit.Assert.fail;

public class CloseableIteratorAdapterUnitTest {

    @Test
    public void nextPassThrough() {
        Iterator<Object> mockIterator = createMockIterator();
        CloseableIterator<Object> iterator = createCloseableIterator(mockIterator);

        try {
            iterator.next();
        } finally {
            Mockito.verify(mockIterator, Mockito.times(1)).next();
            iterator.closeQuietly();
        }
    }

    @Test
    public void hasNextPassThrough() {
        Iterator<Object> mockIterator = createMockIterator();
        CloseableIterator<Object> iterator = createCloseableIterator(mockIterator);

        iterator.hasNext();
        Mockito.verify(mockIterator, Mockito.times(1)).hasNext();
        iterator.closeQuietly();
    }

    // @Test
    public void closeNoopIfNotCloseable() {
        // ??
    }

    @Test
    public void removePassThrough() {
        CloseableIterator<Object> mockIterator = createMockCloseableIterator();
        CloseableIterator<Object> iterator = createCloseableIterator(mockIterator);

        try {
            iterator.remove();
        } finally {
            Mockito.verify(mockIterator, Mockito.times(1)).remove();
            iterator.closeQuietly();
        }
    }

    @Test
    public void closePassThroughIfClosable() throws IOException {
        CloseableIterator<Object> mockIterator = createMockCloseableIterator();
        CloseableIterator<Object> iterator = createCloseableIterator(mockIterator);

        try {
            iterator.close();
        } catch (IOException e) {
            fail();
        } finally {
            Mockito.verify(mockIterator, Mockito.times(1)).close();
        }
    }

    @SuppressWarnings("unchecked")
    private Iterator<Object> createMockIterator() {
        return Mockito.mock(Iterator.class);
    }

    @SuppressWarnings("unchecked")
    private CloseableIterator<Object> createMockCloseableIterator() {
        return Mockito.mock(CloseableIterator.class);
    }

    private <T> CloseableIterator<T> createCloseableIterator(Iterator<T> iterator) {
        return CloseableIteratorAdapter.wrap(iterator);
    }
}
