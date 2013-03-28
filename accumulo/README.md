Mango Accumulo Utilities
==========

Common utilities and classes we have developed for using with Accumulo

## mango.accumulo.util.BatchScannerWithScanners

Implements the BatchScanner interface, but uses straight Scanners underneath instead of starting a thread pool. We implemented this so that we could always use the BatchScanner interface and choose when to initialize new threads and when not to.

### Sample
```java
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
```

## mango.accumulo.pool.ThreadPoolConnector

One problem we constantly run into is that we create too many BatchScanners than our server can handle. This helper class will only create BatchScanners up to a certain thread pool size and then return a BatchScannerWithScanners.

TODO: Keep track of the thread pool with BatchWriters/Deleters as well

TODO: A BatchScanner can be allocated with N threads, but use much less depending on the number of tablets it needs. The current thread pool does not take advantage of this and I'm not sure how it would.

### Sample
```java
    //using thread pool size
    ThreadPoolConnector poolConnector = new ThreadPoolConnector(connector, 10);
```