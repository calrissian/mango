package org.calrissian.mango.collect;

import com.google.common.collect.PeekingIterator;

/**
 * Date: 7/24/12
 * Time: 4:40 PM
 */
public interface PeekingCloseableIterator<T> extends PeekingIterator<T>, CloseableIterator<T>  {
}
