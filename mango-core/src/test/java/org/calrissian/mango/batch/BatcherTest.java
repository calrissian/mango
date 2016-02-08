/*
 * Copyright (C) 2014 The Calrissian Authors
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
package org.calrissian.mango.batch;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import static com.google.common.collect.Iterables.consumingIterable;
import static com.google.common.util.concurrent.MoreExecutors.sameThreadExecutor;
import static java.lang.Thread.sleep;
import static java.util.concurrent.Executors.newCachedThreadPool;
import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class BatcherTest {


    private ExecutorService producerService;

    @Before
    public void setup() {
        producerService = newCachedThreadPool();
    }

    @Test
    public void sizeBatcherTest() throws InterruptedException {

        TestListenter<Integer> listenter = new TestListenter<>();
        final Batcher<Integer> batcher = BatcherBuilder.create()
                .sizeBound(100)
                .build(listenter);

        CountDownLatch start = new CountDownLatch(1);
        CountDownLatch done = setupProducers(batcher, start, 10, 100);
        start.countDown();
        done.await();

        sleep(20);
        batcher.close();

        assertEquals(1000, listenter.getCount());
        assertTrue(listenter.getNumBatches() >= 10);
        for (Collection<Integer> batch : listenter.getBatches()) {
            assertEquals(100, batch.size());
        }
    }

    @Test
    public void sizeBatcherNonFullBatchTest() throws InterruptedException {

        TestListenter<Integer> listenter = new TestListenter<>();
        final Batcher<Integer> batcher = BatcherBuilder.create()
                .sizeBound(100)
                .build(listenter);

        CountDownLatch start = new CountDownLatch(1);
        CountDownLatch done = setupProducers(batcher, start, 10, 102); //don't align with batch size
        start.countDown();
        done.await();

        sleep(20);
        batcher.close();
        sleep(20);

        assertEquals(1020, listenter.getCount());
        assertTrue(listenter.getNumBatches() == 11);
        int count = 0;
        for (Collection<Integer> batch : listenter.getBatches()) {
            if (count == 10) {
                assertEquals(20, batch.size());
            } else {
                assertEquals(100, batch.size());
            }
            count++;
        }
    }

    @Test
    public void timeBatcherTest() throws InterruptedException {

        TestListenter<Integer> listenter = new TestListenter<>();
        final Batcher<Integer> batcher = BatcherBuilder.create()
                .timeBound(10, MILLISECONDS)
                .build(listenter);

        CountDownLatch start = new CountDownLatch(1);
        CountDownLatch done = setupProducers(batcher, start, 10, 100);
        start.countDown();
        done.await();

        //wait double time bound.
        sleep(20);
        batcher.close();

        assertEquals(1000, listenter.getCount());
        assertTrue(listenter.getNumBatches() >= 1);
        for (Collection<Integer> batch : listenter.getBatches()) {
            assertTrue(!batch.isEmpty());
        }
    }

    @Test
    public void sizeAndTimeBatcherTest() throws InterruptedException {

        TestListenter<Integer> listenter = new TestListenter<>();
        final Batcher<Integer> batcher = BatcherBuilder.create()
                .sizeBound(100)
                .timeBound(10, MILLISECONDS)
                .build(listenter);

        CountDownLatch start = new CountDownLatch(1);
        CountDownLatch done = setupProducers(batcher, start, 10, 100);
        start.countDown();
        done.await();

        //wait double time bound.
        sleep(20);
        batcher.close();

        assertEquals(1000, listenter.getCount());
        assertTrue(listenter.getNumBatches() >= 10);
        for (Collection<Integer> batch : listenter.getBatches()) {
            assertTrue(!batch.isEmpty());
            assertTrue(batch.size() <= 100);
        }
    }

    //Tests to verify that ordering is maintained within batchers.
    @Test
    public void sequentialSizeBatcherTest() throws InterruptedException {
        final List<Integer> results = new ArrayList<>(1000);
        final Batcher<Integer> batcher = BatcherBuilder.create()
                .sizeBound(100)
                .listenerService(sameThreadExecutor()) //Required to guarantee ordering
                .build(new BatchListener<Integer>() {
                    @Override
                    public void onBatch(Collection<Integer> batch) {
                        results.addAll(batch);
                    }
                });

        for (int i = 0;i < 1000;i++)
            batcher.add(i);

        Thread.sleep(20);
        batcher.close();

        assertEquals(1000, results.size());
        for (int i = 0;i< 1000;i++)
            assertEquals(i, results.get(i).intValue());
    }

    @Test
    public void sequentialTimeBatcherTest() throws InterruptedException {
        final List<Integer> results = new ArrayList<>(1000);
        final Batcher<Integer> batcher = BatcherBuilder.create()
                .timeBound(10, MILLISECONDS)
                .listenerService(sameThreadExecutor()) //Required to guarantee ordering
                .build(new BatchListener<Integer>() {
                    @Override
                    public void onBatch(Collection<Integer> batch) {
                        results.addAll(batch);
                    }
                });

        for (int i = 0;i < 1000;i++)
            batcher.add(i);

        Thread.sleep(20);
        batcher.close();

        assertEquals(1000, results.size());
        for (int i = 0;i< 1000;i++)
            assertEquals(i, results.get(i).intValue());
    }

    @Test
    public void sequentialSizeOrTimeBatcherTest() throws InterruptedException {
        final List<Integer> results = new ArrayList<>(1000);
        final Batcher<Integer> batcher = BatcherBuilder.create()
                .sizeBound(100)
                .timeBound(10, MILLISECONDS)
                .listenerService(sameThreadExecutor()) //Required to guarantee ordering
                .build(new BatchListener<Integer>() {
                    @Override
                    public void onBatch(Collection<Integer> batch) {
                        results.addAll(batch);
                    }
                });

        for (int i = 0;i < 1000;i++)
            batcher.add(i);

        Thread.sleep(20);
        batcher.close();

        assertEquals(1000, results.size());
        for (int i = 0;i< 1000;i++)
            assertEquals(i, results.get(i).intValue());
    }

    @Test
    public void closeAndFlushTest() throws InterruptedException {
        final List<Integer> results = new ArrayList<>(1000);
        final Batcher<Integer> batcher = BatcherBuilder.create()
                .sizeBound(100)
                .timeBound(1000000, MILLISECONDS)
                .bufferSize(1000)
                .listenerService(sameThreadExecutor()) //Required to guarantee sleep pauses batch thread.
                .build(new BatchListener<Integer>() {
                    @Override
                    public void onBatch(Collection<Integer> batch) {
                        try {
                            results.addAll(batch);
                            //Sleep here to slow down the processing of the batch thread so it can be killed before the second batch.
                            sleep(1000000);
                        } catch (InterruptedException ignored) {}
                    }
                });

        CountDownLatch start = new CountDownLatch(1);
        CountDownLatch done = setupProducers(batcher, start, 10, 100);
        start.countDown();
        done.await();

        sleep(20);
        List<Integer> flushed = batcher.closeAndFlush();

        assertEquals(900, flushed.size());

        //Listener should have received the partial batch
        assertEquals(100, results.size());
    }

    @Test
    public void closeAndFlushAfterHandlerExceptionTest() throws InterruptedException {
        final List<Integer> results = new ArrayList<>(1000);
        final Batcher<Integer> batcher = BatcherBuilder.create()
                .sizeBound(100)
                .timeBound(1000000, MILLISECONDS)
                .bufferSize(1000)
                .listenerService(sameThreadExecutor()) //Required to guarantee sleep pauses batch thread.
                .build(new BatchListener<Integer>() {
                    @Override
                    public void onBatch(Collection<Integer> batch) {
                        results.addAll(batch);
                        try {
                            sleep(1000000);
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                    }
                });

        CountDownLatch start = new CountDownLatch(1);
        CountDownLatch done = setupProducers(batcher, start, 10, 100);
        start.countDown();
        done.await();

        List<Integer> flushed = batcher.closeAndFlush();

        assertEquals(900, flushed.size());

        //Listener should have received the partial batch
        assertEquals(100, results.size());
    }

    //Exception tests
    @Test(expected = IllegalStateException.class)
    public void noSizeOrTimeBound() {
        BatcherBuilder.create()
                .build(new TestListenter<>());
    }

    @Test(expected = IllegalArgumentException.class)
    public void invalidSetSize() {
        BatcherBuilder.create()
                .sizeBound(-1);
    }

    @Test(expected = IllegalStateException.class)
    public void doubleSetSize() {
        BatcherBuilder.create()
                .sizeBound(100)
                .sizeBound(200);
    }

    @Test(expected = IllegalArgumentException.class)
    public void invalidSetTime() {
        BatcherBuilder.create()
                .timeBound(-1, MILLISECONDS);
    }

    @Test(expected = NullPointerException.class)
    public void nullTimeUnit() {
        BatcherBuilder.create()
                .timeBound(10, null);
    }

    @Test(expected = IllegalStateException.class)
    public void doubleSetTime() {
        BatcherBuilder.create()
                .timeBound(10, MILLISECONDS)
                .timeBound(20, MILLISECONDS);
    }

    @Test(expected = IllegalArgumentException.class)
    public void invalidSetBufferSize() {
        BatcherBuilder.create()
                .bufferSize(-1);
    }

    @Test(expected = IllegalStateException.class)
    public void doubleSetBufferSize() {
        BatcherBuilder.create()
                .bufferSize(10)
                .bufferSize(20);
    }

    @Test(expected = NullPointerException.class)
    public void nullListenerService() {
        BatcherBuilder.create()
                .listenerService(null);
    }

    @Test(expected = IllegalStateException.class)
    public void doubleSetListenerService() {
        BatcherBuilder.create()
                .listenerService(sameThreadExecutor())
                .listenerService(sameThreadExecutor());
    }

    @Test(expected = NullPointerException.class)
    public void invalidListener() {
        BatcherBuilder.create()
                .build(null);
    }

    @Test(expected = IllegalStateException.class)
    public void addAfterClose() {
        Batcher<Integer> batcher = BatcherBuilder.create()
                .sizeBound(100)
                .timeBound(10, MILLISECONDS)
                .build(new TestListenter<Integer>());
        batcher.close();
        batcher.add(1);

    }

    @Test(expected = IllegalStateException.class)
    public void addTimeBasedAfterClose() throws InterruptedException {
        Batcher<Integer> batcher = BatcherBuilder.create()
                .sizeBound(100)
                .timeBound(10, MILLISECONDS)
                .build(new TestListenter<Integer>());
        batcher.close();
        batcher.add(1, 10, MILLISECONDS);
    }

    @Test(expected = IllegalStateException.class)
    public void addOrWaiteAfterClose() throws InterruptedException {
        Batcher<Integer> batcher = BatcherBuilder.create()
                .sizeBound(100)
                .timeBound(10, MILLISECONDS)
                .build(new TestListenter<Integer>());
        batcher.close();
        batcher.addOrWait(1);
    }

    @Test(expected = IllegalStateException.class)
    public void handlerExceptionClose() throws InterruptedException {
        final AtomicBoolean wasCalled = new AtomicBoolean(false);
        Batcher<Integer> batcher = BatcherBuilder.create()
                .sizeBound(1)
                .listenerService(sameThreadExecutor()) //Required to send exception to batch thread
                .build(new BatchListener<Integer>() {
                    @Override
                    public void onBatch(Collection<Integer> batch) {
                        wasCalled.set(true);

                        //Force exception that will fall into the handler catch block.
                        throw new RuntimeException();
                    }
                });
        batcher.add(1);

        //Wait to make sure that the batcher has time
        Thread.sleep(100);

        assertTrue(wasCalled.get());
        batcher.add(1);
    }

    @After
    public void cleanup() {
        producerService.shutdownNow();
    }

    private CountDownLatch setupProducers(final Batcher<Integer> batcher, final CountDownLatch startLatch, int numProducers, final int amountProduced) {
        final CountDownLatch latch = new CountDownLatch(numProducers);
        for (int i = 0; i< numProducers; i++) {
            producerService.submit(new Runnable() {
                @Override
                public void run() {
                    try {
                        startLatch.await();

                        for (int i = 0; i < amountProduced; i++)
                            batcher.addOrWait(i);

                        latch.countDown();
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
            });
        }
        return latch;
    }

    private static class TestListenter<T> implements BatchListener<T> {

        private AtomicInteger numBatches = new AtomicInteger(0);
        private AtomicInteger count = new AtomicInteger(0);
        private ConcurrentLinkedQueue<Collection<T>> batches = new ConcurrentLinkedQueue<>();

        @Override
        public void onBatch(Collection<T> batch) {
            numBatches.incrementAndGet();
            count.addAndGet(batch.size());
            batches.offer(batch);

            //System.out.println("Found " + count.get() + " items at " + System.currentTimeMillis());
        }

        public int getNumBatches() {
            return numBatches.get();
        }

        public int getCount() {
            return count.get();
        }

        public Iterable<Collection<T>> getBatches() {
            return consumingIterable(batches);
        }
    }
}
