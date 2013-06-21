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

import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import org.apache.accumulo.core.client.BatchWriter;
import org.apache.accumulo.core.client.Connector;
import org.apache.accumulo.core.client.IteratorSetting;
import org.apache.accumulo.core.client.mock.MockInstance;
import org.apache.accumulo.core.data.*;
import org.apache.accumulo.core.iterators.user.RegExFilter;
import org.apache.accumulo.core.security.Authorizations;
import org.apache.accumulo.core.security.ColumnVisibility;
import org.apache.hadoop.io.Text;
import org.junit.Before;
import org.junit.Test;

import java.util.Iterator;
import java.util.Map;

import static org.junit.Assert.*;

/**
 */
public class BatchScannerWithScannersTest {

    private Connector connector;
    private Authorizations auths;
    private String tableName;

    @Before
    public void setUp() throws Exception {

        tableName = "testTable";
        auths = new Authorizations("A", "B");
        connector = new MockInstance().getConnector("", "".getBytes());

        //create table
        connector.tableOperations().create(tableName);

        BatchWriter batchWriter = connector.createBatchWriter(tableName, 1000l, 100l, 1);

        Mutation row1 = new Mutation("row1");
        row1.put("cf", "cq1", new ColumnVisibility("A"), new Value("".getBytes()));
        row1.put("cf", "cq2", new ColumnVisibility("B"), new Value("".getBytes()));
        batchWriter.addMutation(row1);
        batchWriter.flush();

        Mutation row2 = new Mutation("row2");
        row2.put("cf", "cq1", new ColumnVisibility("A"), new Value("".getBytes()));
        row2.put("cf", "cq2", new ColumnVisibility("B"), new Value("".getBytes()));
        batchWriter.addMutation(row2);
        batchWriter.flush();

        Mutation row3 = new Mutation("row3");
        row3.put("cf", "cq1", new ColumnVisibility("A"), new Value("".getBytes()));
        row3.put("cf", "cq2", new ColumnVisibility("B"), new Value("".getBytes()));
        batchWriter.addMutation(row3);
        batchWriter.flush();

        batchWriter.close();
    }

    @Test
    public void testSetRange() throws Exception {
        //test single range

        BatchScannerWithScanners scanners = new BatchScannerWithScanners(connector, tableName, auths);
        scanners.setRanges(Lists.newArrayList(Range.exact("row1")));

        Iterator<Map.Entry<Key,Value>> iterator = scanners.iterator();
        assertTrue(iterator.hasNext());
        assertTrue(new Key("row1", "cf", "cq1").equals(iterator.next().getKey(), PartialKey.ROW_COLFAM_COLQUAL));
        assertTrue(new Key("row1", "cf", "cq2").equals(iterator.next().getKey(), PartialKey.ROW_COLFAM_COLQUAL));
        assertFalse(iterator.hasNext());

        scanners.close();
    }

    @Test
    public void testSetRanges() throws Exception {
        //test multiple ranges

        BatchScannerWithScanners scanners = new BatchScannerWithScanners(connector, tableName, auths);
        scanners.setRanges(Lists.newArrayList(Range.exact("row1"), Range.exact("row3")));

        Iterator<Map.Entry<Key,Value>> iterator = scanners.iterator();
        assertTrue(iterator.hasNext());
        assertTrue(new Key("row1", "cf", "cq1").equals(iterator.next().getKey(), PartialKey.ROW_COLFAM_COLQUAL));
        assertTrue(new Key("row1", "cf", "cq2").equals(iterator.next().getKey(), PartialKey.ROW_COLFAM_COLQUAL));
        assertTrue(new Key("row3", "cf", "cq1").equals(iterator.next().getKey(), PartialKey.ROW_COLFAM_COLQUAL));
        assertTrue(new Key("row3", "cf", "cq2").equals(iterator.next().getKey(), PartialKey.ROW_COLFAM_COLQUAL));
        assertFalse(iterator.hasNext());

        scanners.close();
    }

    @Test
    public void testSetRegex() throws Exception {
        //test multiple ranges and regex filter

        BatchScannerWithScanners scanners = new BatchScannerWithScanners(connector, tableName, auths);
        scanners.setRanges(Lists.newArrayList(Range.exact("row1"), Range.exact("row3")));

        IteratorSetting regexFilter = new IteratorSetting(10, "regexFilter", RegExFilter.class.getName());
        RegExFilter.setRegexs(regexFilter, null, null, "cq2", null, false);
        scanners.addScanIterator(regexFilter);

        assertEquals(2, Iterables.size(scanners));

        scanners.close();
    }

    @Test
    public void testSetFetchColumn() throws Exception {

        BatchScannerWithScanners scanners = new BatchScannerWithScanners(connector, tableName, auths);
        scanners.setRanges(Lists.newArrayList(Range.exact("row1"), Range.exact("row3")));
        scanners.fetchColumn(new Text("cf"), new Text("cq1"));

        assertEquals(2, Iterables.size(scanners));

        scanners.close();
    }

    @Test
    public void testNoDataReturned() throws Exception {
        BatchScannerWithScanners scanners = new BatchScannerWithScanners(connector, tableName, auths);
        scanners.setRanges(Lists.newArrayList(Range.exact("row100"), Range.exact("row300")));

        assertEquals(0, Iterables.size(scanners));

        scanners.close();
    }
}
