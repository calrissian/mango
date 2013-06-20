package org.calrissian.mango.accumulo.pool;

import org.apache.accumulo.core.client.BatchScanner;
import org.apache.accumulo.core.client.IteratorSetting;
import org.apache.accumulo.core.data.Key;
import org.apache.accumulo.core.data.Range;
import org.apache.accumulo.core.data.Value;
import org.apache.hadoop.io.Text;

import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

/**
 */
public class BatchScannerThreadedResource implements BatchScanner, ThreadedResource {

    private final BatchScanner batchScanner;
    private final ThreadPoolConnector threadPoolConnector;
    private final int numThreads;

    public BatchScannerThreadedResource(BatchScanner batchScanner, ThreadPoolConnector threadPoolConnector, int numThreads) {
        this.batchScanner = batchScanner;
        this.threadPoolConnector = threadPoolConnector;
        this.numThreads = numThreads;
    }

    @Override
    public void setRanges(Collection<Range> ranges) {
        batchScanner.setRanges(ranges);
    }

    @Override
    public void close() {
        batchScanner.close();
        threadPoolConnector.releaseResources(this);
    }

    @Override
    public void addScanIterator(IteratorSetting cfg) {
        batchScanner.addScanIterator(cfg);
    }

    @Override
    public void removeScanIterator(String iteratorName) {
        batchScanner.removeScanIterator(iteratorName);
    }

    @Override
    public void updateScanIteratorOption(String iteratorName, String key, String value) {
        batchScanner.updateScanIteratorOption(iteratorName, key, value);
    }

    @Override
    public void setScanIterators(int priority, String iteratorClass, String iteratorName) throws IOException {
        batchScanner.setScanIterators(priority, iteratorClass, iteratorName);
    }

    @Override
    public void setScanIteratorOption(String iteratorName, String key, String value) {
        batchScanner.setScanIteratorOption(iteratorName, key, value);
    }

    @Override
    public void setupRegex(String iteratorName, int iteratorPriority) throws IOException {
        batchScanner.setupRegex(iteratorName, iteratorPriority);
    }

    @Override
    public void setRowRegex(String regex) {
        batchScanner.setRowRegex(regex);
    }

    @Override
    public void setColumnFamilyRegex(String regex) {
        batchScanner.setColumnFamilyRegex(regex);
    }

    @Override
    public void setColumnQualifierRegex(String regex) {
        batchScanner.setColumnQualifierRegex(regex);
    }

    @Override
    public void setValueRegex(String regex) {
        batchScanner.setValueRegex(regex);
    }

    @Override
    public void fetchColumnFamily(Text col) {
        batchScanner.fetchColumnFamily(col);
    }

    @Override
    public void fetchColumn(Text colFam, Text colQual) {
        batchScanner.fetchColumn(colFam, colQual);
    }

    @Override
    public void clearColumns() {
        batchScanner.clearColumns();
    }

    @Override
    public void clearScanIterators() {
        batchScanner.clearScanIterators();
    }

    @Override
    public Iterator<Map.Entry<Key, Value>> iterator() {
        return batchScanner.iterator();
    }

    @Override
    public int getNumThreads() {
        return numThreads;
    }
}
