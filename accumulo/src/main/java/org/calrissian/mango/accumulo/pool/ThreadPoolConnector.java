package org.calrissian.mango.accumulo.pool;

import com.google.common.base.Preconditions;
import org.calrissian.mango.accumulo.util.BatchScannerWithScanners;
import org.apache.accumulo.core.client.*;
import org.apache.accumulo.core.client.admin.InstanceOperations;
import org.apache.accumulo.core.client.admin.SecurityOperations;
import org.apache.accumulo.core.client.admin.TableOperations;
import org.apache.accumulo.core.security.Authorizations;

/**
 * Return BatchScanners and writers based on the number of max threads available. After a limit is hit, return only Scanners and
 * writers with 1 thread.
 * <p/>
 * TODO: Implement pooling with the BatchWriter
 */
public class ThreadPoolConnector extends Connector {

    private final Connector wrapped;
    private final int maxThreads;
    private int usedThreads = 0;
    private final Object lock = new Object();

    /**
     * @param wrapped    Accumulo Connector to wrap
     * @param maxThreads Maximum Threads allowed to create. Must be > 0
     */
    public ThreadPoolConnector(Connector wrapped, int maxThreads) {
        Preconditions.checkArgument(maxThreads > 0);
        this.wrapped = wrapped;
        this.maxThreads = maxThreads;
    }

    /**
     * Allocate the number of threads from the pool. If none are available, return 0. Otherwise return the maximum allowed,
     * up to the numQueryThreads supplied.
     *
     * @param numQueryThreads Number of threads to allocate. Must be > 0
     * @return Allocated amount of threads
     */
    protected int allocateNumThreads(int numQueryThreads) {
        Preconditions.checkArgument(numQueryThreads > 0, "Number of query threads must be greater than 0");
        synchronized (lock) {
            int availableThreads = maxThreads - usedThreads;
            //if none are available
            if (availableThreads == 0) {
                return 0;
            }
            //if we are going above our alloted max, return the amount left
            if (numQueryThreads > availableThreads) {
                usedThreads = maxThreads;
                return availableThreads;
            }

            //increase poolsize
            usedThreads += numQueryThreads;
            return numQueryThreads;
        }
    }

    /**
     * Release the number of threads.
     *
     * @param tr The threaded resource to release
     */
    protected void releaseResources(ThreadedResource tr) {
        Preconditions.checkNotNull(tr);
        int numThreads = tr.getNumThreads();
        Preconditions.checkArgument(numThreads > 0);

        synchronized (lock) {
            int newPoolSize = usedThreads - numThreads;
            if (newPoolSize < 0) {
                newPoolSize = 0; //not sure how this is possible, unless we are not maintaining the count properly
            }
            usedThreads = newPoolSize;
        }
    }

    /**
     * On creation of a batchscanner, if there are > 0 threads available in the pool, return a BatchScanner. Otherwise, return
     * the {@link BatchScannerWithScanners}, a BatchScanner backed by Scanners.
     */
    @Override
    public BatchScanner createBatchScanner(String tableName, Authorizations authorizations, int numQueryThreads) throws TableNotFoundException {
        numQueryThreads = allocateNumThreads(numQueryThreads);
        if (numQueryThreads > 0) {
            return new BatchScannerThreadedResource(wrapped.createBatchScanner(tableName, authorizations, numQueryThreads),
                    this, numQueryThreads);
        } else {
            return new BatchScannerWithScanners(this, tableName, authorizations);
        }
    }

    @Override
    public BatchDeleter createBatchDeleter(String tableName, Authorizations authorizations, int numQueryThreads, long maxMemory, long maxLatency, int maxWriteThreads) throws TableNotFoundException {
        return wrapped.createBatchDeleter(tableName, authorizations, numQueryThreads, maxMemory, maxLatency, maxWriteThreads);
    }

    @Override
    public BatchWriter createBatchWriter(String tableName, long maxMemory, long maxLatency, int maxWriteThreads) throws TableNotFoundException {
        return wrapped.createBatchWriter(tableName, maxMemory, maxLatency, maxWriteThreads);
    }

    @Override
    public MultiTableBatchWriter createMultiTableBatchWriter(long maxMemory, long maxLatency, int maxWriteThreads) {
        return wrapped.createMultiTableBatchWriter(maxMemory, maxLatency, maxWriteThreads);
    }

    @Override
    public Scanner createScanner(String tableName, Authorizations authorizations) throws TableNotFoundException {
        return wrapped.createScanner(tableName, authorizations);
    }

    @Override
    public Instance getInstance() {
        return wrapped.getInstance();
    }

    @Override
    public String whoami() {
        return wrapped.whoami();
    }

    @Override
    public synchronized TableOperations tableOperations() {
        return wrapped.tableOperations();
    }

    @Override
    public synchronized SecurityOperations securityOperations() {
        return wrapped.securityOperations();
    }

    @Override
    public synchronized InstanceOperations instanceOperations() {
        return wrapped.instanceOperations();
    }

    /**
     * Get the maximum number of threads allowed
     *
     * @return
     */
    public int getMaxThreads() {
        return maxThreads;
    }

    /**
     * Get the current number of threads being used
     * @return
     */
    public int getUsedThreads() {
        return usedThreads;
    }

    /**
     * Get the number of threads left in the pool
     * @return
     */
    public int getNumAvailable() {
        return maxThreads - usedThreads;
    }
}
