package com.texeltek.mango.collect;

import org.junit.Test;
import org.mockito.Mockito;

import java.io.IOException;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

import static org.junit.Assert.*;

/**
 * Collection of tests of {@link com.texeltek.mango.collect.LimitCloseableIterator}
 */
public class LimitIteratorCloseableUnitTest {

    @Test(expected = IllegalArgumentException.class)
    public void constructorThrowsIllegalArgumentExceptionIfLimitIsBelowZero() {
        new LimitCloseableIterator<Object>(newEmptyIterator(), -1);
    }

    @Test
    public void hasNextReturnsFalseIfIteratorIsEmpty() {
        CloseableIterator<Object> iter = new LimitCloseableIterator<Object>(newEmptyIterator(), 10);
        assertFalse(iter.hasNext());
        iter.closeQuietly();
    }

    @Test(expected = NoSuchElementException.class)
    public void nextThrowsNoSuchElementExceptionIfIteratorIsEmpty() {
        CloseableIterator<Object> iter = new LimitCloseableIterator<Object>(newEmptyIterator(), 10);
        try {
            iter.next();
        } finally {
            iter.closeQuietly();
        }
    }

    @Test
    public void removePassesThrough() {
        CloseableIterator<Object> mockedIterator = newMockIterator();
        CloseableIterator<Object> iter = new LimitCloseableIterator<Object>(mockedIterator, 10);
        try {
            iter.remove();
        } finally {
            Mockito.verify(mockedIterator).remove();
            iter.closeQuietly();
        }
    }

    @Test
    public void hasNextCalledIfLimitNotExceeded() {
        CloseableIterator<Object> mockedIterator = newMockIterator();
        CloseableIterator<Object> iter = new LimitCloseableIterator<Object>(mockedIterator, 1);
        iter.hasNext();
        Mockito.verify(mockedIterator).hasNext();
        iter.closeQuietly();
    }

    @Test
    public void hasNextReturnsFalseIfLimitIsZero() {
        CloseableIterator<Object> mockedIterator = newMockIterator();
        Mockito.when(mockedIterator.hasNext()).thenReturn(true);

        CloseableIterator<Object> iter = new LimitCloseableIterator<Object>(mockedIterator, 0);
        assertFalse(iter.hasNext());
        iter.closeQuietly();
    }

    @Test
    public void nextThrowsNoSuchElementExceptionIfLimitIsZero() {
        CloseableIterator<Object> mockedIterator = newMockIterator();
        CloseableIterator<Object> iter = new LimitCloseableIterator<Object>(mockedIterator, 0);
        try {
            iter.next();
            fail("expected NoSuchElementException to be thrown?");
        } catch (NoSuchElementException ex) {
            // expected
        } finally {
            Mockito.verify(mockedIterator, Mockito.never()).next();
            iter.closeQuietly();
        }
    }

    @Test
    public void nextReturnsResultIfLimitNotExceeded() {
        List<Integer> list = Arrays.asList(1, 2);
        CloseableIterator<Integer> iter = new LimitCloseableIterator<Integer>(createCloseableIterator(list.iterator()), 2);
        try {
            assertEquals(1, iter.next().intValue());
            assertEquals(2, iter.next().intValue());
        } finally {
            iter.closeQuietly();
        }
    }

    @Test(expected = NoSuchElementException.class)
    public void nextThrowsNoSuchElementExceptionIfLimitExceeded() {
        CloseableIterator<Object> mockedIterator = newMockIterator();
        Mockito.when(mockedIterator.hasNext()).thenReturn(true);

        CloseableIterator<Object> iter = new LimitCloseableIterator<Object>(mockedIterator, 2);
        try {
            iter.next();
            iter.next();
        } catch (NoSuchElementException ex) {
            fail("should not throw an exception since the limit wasn't exceeded");
        }

        try {
            iter.next();
        } finally {
            iter.closeQuietly();
        }
    }

    @Test
    public void hasNextReturnsFalseIfLimitExceeded() {
        CloseableIterator<Object> mockedIterator = newMockIterator();
        Mockito.when(mockedIterator.hasNext()).thenReturn(true);

        CloseableIterator<Object> iter = new LimitCloseableIterator<Object>(mockedIterator, 1);
        try {
            assertTrue(iter.hasNext());
            iter.next();
            assertFalse(iter.hasNext());
        } finally {
            iter.closeQuietly();
        }
    }

    @Test
    public void closePassesThroughToInnerIterator() throws IOException {
        CloseableIterator<Object> mockIterator = newMockIterator();
        CloseableIterator<Object> iter = new LimitCloseableIterator<Object>(mockIterator, 1);

        try {
            iter.close();
        } catch (IOException e) {
            fail();
        } finally {
            Mockito.verify(mockIterator, Mockito.times(1)).close();
            iter.closeQuietly();
        }
    }

    @SuppressWarnings("unchecked")
    private CloseableIterator<Object> newEmptyIterator() {
        return CloseableIteratorAdapter.<Object>emptyIterator();
    }

    @SuppressWarnings("unchecked")
    private CloseableIterator<Object> newMockIterator() {
        return Mockito.mock(CloseableIterator.class);
    }

    @Test(expected = NoSuchElementException.class)
    public void nextThrowsNoSuchElementExceptionIfIteratorIsExhaustedAndLimitNotExceeded() {
        List<Integer> list = Arrays.asList(1, 2);
        CloseableIterator<Integer> iter = new LimitCloseableIterator<Integer>(createCloseableIterator(list.iterator()), 5);
        try {
            iter.next();
            iter.next();
        } catch (NoSuchElementException ex) {
            fail("should not throw an exception since the limit wasn't exceeded and there are more elements in the list");
        }
        iter.next();
    }

    private <T> CloseableIterator<T> createCloseableIterator(Iterator<T> iterator) {
        return CloseableIteratorAdapter.<T>wrap(iterator);
    }
}
