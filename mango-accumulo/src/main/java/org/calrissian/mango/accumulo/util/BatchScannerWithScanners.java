package org.calrissian.mango.accumulo.util;

import com.google.common.base.Function;
import com.google.common.base.Preconditions;
import com.google.common.collect.Iterables;
import org.apache.accumulo.core.client.*;
import org.apache.accumulo.core.client.impl.ScannerOptions;
import org.apache.accumulo.core.data.Column;
import org.apache.accumulo.core.data.Key;
import org.apache.accumulo.core.data.Range;
import org.apache.accumulo.core.data.Value;
import org.apache.accumulo.core.data.thrift.IterInfo;
import org.apache.accumulo.core.security.Authorizations;
import org.apache.hadoop.io.Text;
import org.calrissian.mango.accumulo.exception.AlreadyClosedException;

import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.Map;

/**
 * A BatchScanner implementation backed with Scanners. This is a non threaded implementation of the BatchScanner.
 */
public class BatchScannerWithScanners extends ScannerOptions implements BatchScanner {

    private final Connector connector;
    private final String tableName;
    private final Authorizations authorizations;
    private Collection<Range> ranges;
    private boolean open = true;

    public BatchScannerWithScanners(Connector connector, String tableName, Authorizations authorizations) {
        this.connector = connector;
        this.tableName = tableName;
        this.authorizations = authorizations;
    }

    @Override
    public void setRanges(Collection<Range> ranges) {
        Preconditions.checkNotNull(ranges);
        this.ranges = Collections.unmodifiableCollection(ranges);
    }

    @Override
    public void close() {
        verifyOpen();
        open = false;
    }

    @Override
    public Iterator<Map.Entry<Key, Value>> iterator() {
        return Iterables.concat(
                Iterables.transform(ranges,
                        new Function<Range, Iterable<Map.Entry<Key, Value>>>() {
                            @Override
                            public Iterable<Map.Entry<Key, Value>> apply(Range range) {
                                try {
                                    Scanner scanner = connector.createScanner(tableName, authorizations);
                                    scanner.setRange(range);

                                    //add iterator settings
                                    for(IterInfo iterInfo : serverSideIteratorList) {
                                        String iterName = iterInfo.getIterName();
                                        IteratorSetting iteratorSetting = new IteratorSetting(iterInfo.getPriority(), iterName, iterInfo.getClassName());
                                        iteratorSetting.addOptions(serverSideIteratorOptions.get(iterName));
                                        scanner.addScanIterator(iteratorSetting);
                                    }

                                    //add fetched columns
                                    for(Column column : fetchedColumns) {
                                        scanner.fetchColumn(new Text(column.getColumnFamily()), new Text(column.getColumnQualifier()));
                                    }
                                    return scanner;
                                } catch (TableNotFoundException e) {
                                    throw new RuntimeException(e);
                                }
                            }
                        }
                )
        ).iterator();
    }

    private void verifyOpen() {
        if (!open) throw new AlreadyClosedException("Scanner already closed");
    }

    public boolean isOpen() {
        return open;
    }
}
