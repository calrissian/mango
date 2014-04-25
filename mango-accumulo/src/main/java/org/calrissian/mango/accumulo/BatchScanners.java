package org.calrissian.mango.accumulo;

import org.apache.accumulo.core.client.BatchScanner;
import org.apache.accumulo.core.data.Key;
import org.apache.accumulo.core.data.Value;
import org.calrissian.mango.collect.CloseableIterable;
import org.calrissian.mango.collect.FluentCloseableIterable;

import java.io.IOException;
import java.util.Iterator;

import static java.util.Map.Entry;

public class BatchScanners {

    private BatchScanners() {/* private constructor */}

    /**
     * Converts a {@link BatchScanner} into a {@link CloseableIterable}
     */
    public static CloseableIterable<Entry<Key, Value>> closeableIterable(final BatchScanner batchScanner) {
        return new FluentCloseableIterable<Entry<Key, Value>>() {
            @Override
            protected void doClose() throws IOException {
                batchScanner.close();
            }

            @Override
            protected Iterator<Entry<Key, Value>> retrieveIterator() {
                return batchScanner.iterator();
            }
        };
    }

}
