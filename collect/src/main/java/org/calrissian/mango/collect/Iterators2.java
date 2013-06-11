package org.calrissian.mango.collect;


import com.google.common.collect.AbstractIterator;

/**
 * Additional functions for working on Iterators
 */
import java.util.Iterator;

public class Iterators2 {

    public static <T> Iterator<T> distinct(final Iterator<T> iterator) {
        return new AbstractIterator<T>() {
            T current = null;
            @Override
            protected T computeNext() {
                if (iterator.hasNext()) {
                    if (current == null) {
                        current = iterator.next();
                        return current;
                    } else {
                        T next = iterator.next();
                        while (current.equals(next)) {
                            if (iterator.hasNext()) {
                                next = iterator.next();
                            } else {
                                return endOfData();
                            }
                        }
                        current = next;
                        return current;
                    }
                } else
                    return endOfData();
            }
        };

    }
}
