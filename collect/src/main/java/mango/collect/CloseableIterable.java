package mango.collect;

import java.io.Closeable;

/**
 * Iterable that is closeable to release resources
 */
public interface CloseableIterable<T> extends Iterable<T>, Closeable {

    public void closeQuietly();

}
