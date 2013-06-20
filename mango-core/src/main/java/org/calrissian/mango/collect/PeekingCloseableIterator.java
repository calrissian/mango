package org.calrissian.mango.collect;

import com.google.common.collect.PeekingIterator;

/**
 * A PeekingIterator which is also a CloseableIterator.
 * @param <T>
 */
public interface PeekingCloseableIterator<T> extends PeekingIterator<T>, CloseableIterator<T>  {
}
