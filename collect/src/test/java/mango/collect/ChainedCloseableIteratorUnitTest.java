package mango.collect;

import org.junit.Test;
import org.mockito.Mockito;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

import static org.junit.Assert.*;

public class ChainedCloseableIteratorUnitTest {

    @Test
    public void emptyIteratorHasNextReturnsFalse() {
        CloseableIterator iterator = new ChainedCloseableIterator<Object>(emptyIterator(), emptyIterator(), emptyIterator());
        assertFalse(iterator.hasNext());
        iterator.closeQuietly();
    }

    @Test(expected = NoSuchElementException.class)
    public void emptyIteratorNextThrowsNoSuchElementException() {
        CloseableIterator iterator = new ChainedCloseableIterator<Object>(emptyIterator(), emptyIterator(), emptyIterator());
        try {
            iterator.next();
        } finally {
            iterator.closeQuietly();
        }
    }

    @Test
    public void nonEmptyIteratorHasNextReturnsTrue() {
        CloseableIterator iterator = new ChainedCloseableIterator<Object>(newIterator(1));
        assertTrue(iterator.hasNext());
        iterator.closeQuietly();
    }

    @Test
    public void nonEmptyIteratorNextDoesNotThrowNoSuchElementException() {
        CloseableIterator iterator = new ChainedCloseableIterator<Object>(newIterator(1));
        try {
            assertTrue(iterator.hasNext());
            iterator.next();
        } catch (NoSuchElementException ex) {
            fail("should not throw NoSuchElementException here");
        } finally {
            iterator.closeQuietly();
        }
    }

    @Test
    public void exhaustIterators() {
        CloseableIterator iterator1 = Mockito.spy(newIterator(1));
        CloseableIterator iterator2 = Mockito.spy(newIterator(2));
        CloseableIterator iterator3 = Mockito.spy(newIterator(3));
        CloseableIterator chainedIterator = new ChainedCloseableIterator<Object>(iterator1, iterator2, iterator3);

        while (chainedIterator.hasNext()) {
            chainedIterator.next();
        }
        Mockito.verify(iterator1, Mockito.times(1)).next();
        Mockito.verify(iterator2, Mockito.times(2)).next();
        Mockito.verify(iterator3, Mockito.times(3)).next();
    }

    @Test
    public void iteratorOrderMatters() {
        CloseableIterator iterator1 = Mockito.spy(newIterator(1));
        CloseableIterator iterator2 = Mockito.spy(newIterator(2));
        CloseableIterator iterator3 = Mockito.spy(newIterator(3));
        CloseableIterator chainedIterator = new ChainedCloseableIterator<Object>(iterator1, iterator2, iterator3);

        int count = 0;
        while (chainedIterator.hasNext()) {
            Mockito.verify(iterator1, Mockito.times(Math.min(count, 1))).next();
            Mockito.verify(iterator2, Mockito.times(Math.min(Math.max(count - 1, 0), 2))).next();
            Mockito.verify(iterator3, Mockito.times(Math.min(Math.max(count - 3, 0), 3))).next();
            chainedIterator.next();
            ++count;
        }
        Mockito.verify(iterator1, Mockito.times(1)).next();
        Mockito.verify(iterator2, Mockito.times(2)).next();
        Mockito.verify(iterator3, Mockito.times(3)).next();
    }

    @Test
    public void closeCallsCloseOnAllIterators() throws IOException {
        CloseableIterator iterator1 = Mockito.spy(newIterator(1));
        CloseableIterator iterator2 = Mockito.spy(newIterator(2));
        CloseableIterator iterator = new ChainedCloseableIterator<Object>(iterator1, iterator2);

        iterator.close();
        Mockito.verify(iterator1).close();
        Mockito.verify(iterator2).close();
    }

    @Test
    public void removeCallsRemoveOnCurrentIterator() {
        CloseableIterator iterator1 = newMockIterator();
        CloseableIterator iterator2 = newMockIterator();
        CloseableIterator iterator = new ChainedCloseableIterator<Object>(iterator1, iterator2);

        iterator.next();
        iterator.remove();
        Mockito.verify(iterator1, Mockito.times(1)).remove();
        Mockito.verify(iterator2, Mockito.never()).remove();
        iterator.closeQuietly();
    }

    @Test
    public void removeCallsRemoveOnIteratorThatReturnedOnNextCall() {
        CloseableIterator notMe1 = Mockito.spy(emptyIterator());
        CloseableIterator notMe2 = Mockito.spy(emptyIterator());
        CloseableIterator me = newMockIterator();
        CloseableIterator notMe3 = Mockito.spy(emptyIterator());
        CloseableIterator iterator = new ChainedCloseableIterator<Object>(notMe1, notMe2, me, notMe3);

        iterator.next();
        iterator.remove();
        Mockito.verify(notMe1, Mockito.never()).remove();
        Mockito.verify(notMe2, Mockito.never()).remove();
        Mockito.verify(me, Mockito.times(1)).remove();
        Mockito.verify(notMe3, Mockito.never()).remove();
        iterator.closeQuietly();
    }

    @Test(expected = UnsupportedOperationException.class)
    public void removeThrowsIllegalStateExceptionIfNextIsNotCalled() {
        CloseableIterator iterator = new ChainedCloseableIterator<Object>(newIterator(1));

        iterator.remove();
        fail("expected an UnsupportedOperationException to be thrown?");
    }

    @SuppressWarnings("unchecked")
    private CloseableIterator newIterator(int numberOfElements) {
        List list = new ArrayList(numberOfElements);
        for (int ii = 0; ii < numberOfElements; ++ii) {
            list.add(Mockito.mock(Object.class));
        }
        return createCloseableIterator(list.iterator());
    }

    private CloseableIterator emptyIterator() {
        return CloseableIteratorAdapter.emptyIterator();
    }

    private <T> CloseableIterator<? extends T> createCloseableIterator(Iterator<? extends T> iterator) {
        return CloseableIteratorAdapter.wrap(iterator);
    }

    @SuppressWarnings("unchecked")
    private CloseableIterator newMockIterator() {
        CloseableIterator mockIterator = mockCloseableIterator();
        Object mockObject = Mockito.mock(Object.class);
        Mockito.when(mockIterator.hasNext()).thenReturn(true);
        Mockito.when(mockIterator.next()).thenReturn(mockObject);

        return mockIterator;
    }

    @SuppressWarnings("unchecked")
    private CloseableIterator mockCloseableIterator() {
        return Mockito.mock(CloseableIterator.class);
    }
}
