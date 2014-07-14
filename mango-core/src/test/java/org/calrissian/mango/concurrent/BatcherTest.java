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
package org.calrissian.mango.concurrent;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Collection;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.atomic.AtomicInteger;

import static com.google.common.collect.Iterables.consumingIterable;
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

        TestListenter<Integer> listenter = new TestListenter<Integer>();
        final Batcher<Integer> batcher = BatcherBuilder.create()
                .maxSize(100)
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

        TestListenter<Integer> listenter = new TestListenter<Integer>();
        final Batcher<Integer> batcher = BatcherBuilder.create()
                .maxSize(100)
                .build(listenter);

        CountDownLatch start = new CountDownLatch(1);
        CountDownLatch done = setupProducers(batcher, start, 10, 102); //don't align with batch size
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
    public void timeBatcherTest() throws InterruptedException {

        TestListenter<Integer> listenter = new TestListenter<Integer>();
        final Batcher<Integer> batcher = BatcherBuilder.create()
                .maxTime(10, MILLISECONDS)
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

        TestListenter<Integer> listenter = new TestListenter<Integer>();
        final Batcher<Integer> batcher = BatcherBuilder.create()
                .maxSize(100)
                .maxTime(10, MILLISECONDS)
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
                        e.printStackTrace();
                    }
                }
            });
        }
        return latch;
    }

    private static class TestListenter<T> implements BatchListener<T> {

        private AtomicInteger numBatches = new AtomicInteger(0);
        private AtomicInteger count = new AtomicInteger(0);
        private ConcurrentLinkedQueue<Collection<T>> batches = new ConcurrentLinkedQueue<Collection<T>>();

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
