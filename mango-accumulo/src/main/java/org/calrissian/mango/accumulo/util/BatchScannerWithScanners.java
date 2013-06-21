/*
 * Copyright (C) 2013 The Calrissian Authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.calrissian.mango.accumulo.util;

import com.google.common.base.Function;
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

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

import static com.google.common.base.Preconditions.checkNotNull;
import static java.util.Collections.unmodifiableCollection;

/**
 * A BatchScanner implementation backed with Scanners. This is a non threaded implementation of the BatchScanner.
 */
public class BatchScannerWithScanners extends ScannerOptions implements BatchScanner {

    private final Connector connector;
    private final String tableName;
    private final Authorizations authorizations;
    private Collection<Range> ranges;

    public BatchScannerWithScanners(Connector connector, String tableName, Authorizations authorizations) {
        this.connector = connector;
        this.tableName = tableName;
        this.authorizations = authorizations;
    }

    @Override
    public void setRanges(Collection<Range> ranges) {
        checkNotNull(ranges);
        this.ranges = unmodifiableCollection(ranges);
    }

    @Override
    public void close() {
        //No held resources
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
}
