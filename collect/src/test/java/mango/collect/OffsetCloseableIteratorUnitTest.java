package mango.collect;

import com.google.common.collect.Iterators;
import org.junit.Test;
import org.mockito.Mockito;

import java.io.IOException;

import static org.junit.Assert.*;

public class OffsetCloseableIteratorUnitTest {
    
    @Test(expected=IllegalArgumentException.class)
    public void constructorThrowsIllegalArgumentExceptionIfOffsetIsLessThanZero() {
        new OffsetCloseableIterator<String>(null, -1);
    }
    
    @Test
    public void hasNextReturnsTrueIfThereAreElementsAfterTheOffset() {
        CloseableIterator<String> iterator = Mockito.mock(CloseableIterator.class);
        
        Mockito.when(iterator.hasNext()).thenReturn(true).thenReturn(true).thenReturn(false);
        
        OffsetCloseableIterator<String> offsetIterator = new OffsetCloseableIterator<String>(iterator, 1);
        
        assertTrue(offsetIterator.hasNext());
    }
    
    @Test
    public void hasNextReturnsFalseIfOffsetExceedsLengthOfIterator() {
        CloseableIterator<String> iterator = Mockito.mock(CloseableIterator.class);
        
        Mockito.when(iterator.hasNext()).thenReturn(true).thenReturn(true).thenReturn(false);
        
        OffsetCloseableIterator<String> offsetIterator = new OffsetCloseableIterator<String>(iterator, 2);
        
        assertFalse(offsetIterator.hasNext());
    }
    
    @Test
    public void removePassesThrough() {
        CloseableIterator<String> iterator = Mockito.mock(CloseableIterator.class);
        
        OffsetCloseableIterator<String> offsetIterator = new OffsetCloseableIterator<String>(iterator, 2);
        offsetIterator.remove();
        
        Mockito.verify(iterator, Mockito.times(1)).remove();
    }
    
    @Test
    public void closePassesThrough() throws IOException {
        CloseableIterator<String> iterator = Mockito.mock(CloseableIterator.class);
        
        OffsetCloseableIterator<String> offsetIterator = new OffsetCloseableIterator<String>(iterator, 2);
        offsetIterator.close();
        
        Mockito.verify(iterator, Mockito.times(1)).close();
    }
    
    @Test
    public void firstCallToNextReturnsTheFirstElementAfterTheOffset() {
        CloseableIterator<String> iterator = CloseableIteratorAdapter.wrap(Iterators.forArray("a", "b", "c"));
        OffsetCloseableIterator<String> offsetIterator = new OffsetCloseableIterator<String>(iterator, 2);
        
        assertEquals("c", offsetIterator.next());
    }
}
