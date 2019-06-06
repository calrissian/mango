[![Build Status](https://travis-ci.org/calrissian/mango.svg?branch=master)](https://travis-ci.org/calrissian/mango)

Calrissian Mango
================
![mango](http://www.gallafoods.com/images/mango.png "Mango")

Mango provides common reusable utilities for the cloud toolkit.

###Batchers
The Batcher utilities provide an easy to configure means of generating batches of data triggered based on size and time. 
These are designed to work in a multi-threaded environment making it easy to integrate anywhere a simple batching solution
is needed.

The following is an example for triggering a new batch when it hits 100 elements or 10 seconds have elapsed.
```java
Batcher batcher = BatcherBuilder.create()
    .sizeBound(100)
    .timeBound(10, SECONDS)
    .build(batch -> System.out.println("Do something with batch"));

```

###Collect
The collect module provides many tools for working with Iterators and Iterables. Most of these utilities provide extensions
to the capabilities provided by Guava. 

The primary capability provided by these tools is the inclusion of the ```CloseableIterator``` and the ```CloseableIterable``` iterfaces
which provide utilities for working with data sets which may be backed by a closeable resource.  This is very useful when
working with large databases or third party systems where streaming data lazily is a better access pattern.  By wrapping,
the results in a Closeable resource, the utilities provided in the ```CloseableIterators``` and ```CloseableIterables``` classes allow easy
manipulation of these datasets while still being able to close the underlying resource.

```java
//Close with try-with-resources block to autoclose data
try (CloseableIterables<String> data = CloseableIterable.limit(rawData, 100)) {
    for (String value : data) {
        System.out.println("Do somethting with value");   
    }
}

``` 

There is also a ```FluentCloseableIterable``` with is designed to be very similar to Guava's ```FluentIterable```. This 
utilitity is useful to use in conjunction with the ```autoClose``` method to close the underlying resource once the data
has been exhausted.

```java
//autoClose() will close the resource in 'rawData' if there is an exception or when done building the list.
List<Integer> data = FluentCloseableIterables.from(rawData)
    .filter(Objects::nonNull)
    .limit(100)
    .transform(v -> Integer.parseInt(v))
    .autoClose()
    .toList(); 
``` 

###IO
There are a few simple utilities for working with IO. The `Serializables` utilities are helpers for converting Java serialiable
objects to and from byte arrays. The abstract buffered streams are utilities for chunking up streams and reconstituting 
that data later.

###Net
The ```net``` package provides utilities working with IP addresses and Java's InetAddress classes.  The ```IPv4``` and ```IPv6```
classes are lightweight wrappers around Java's Inet4address and Inet6Address classes respectively, with the primary goal
of making them Comparable. This makes these classes much easier to use to for things like range checks.  For instance, 
an instance of Guava's ```Range``` can be constructed from a CIDR block which can be used to test IP's.

```java
Range<IPv4> cidr = IPv4.cidrRange("10.0.0.0/8");

//Just check the ip within the range.
boolean containsIP = cidr.contains(IPv4.fromString("10.1.1.1"));
```

The ```MoreInetAddresses``` utilities are designed to provide an extension to Guava's ```InetAddresses```.  In addition 
to tools such as CIDR parsing the ```MoreInetAddresses``` also provides several utilities to work around the fact that
Java will automatically convert "*IPv4 Mapped*" IPv6 addresses to an ```Inet4Address```.   This behavior isinhereted by 
the ```InetAddresses``` utilities in Guava.  ```MoreInetAddresses``` utilities will always preserve the original ipv6 
address as an Inet6Address irregardless of if it represents a "*IPv4 Mapped*" address.

###Types
One of the primary utilities are the type encoders. These are a set of utilities for encoding and decoding data in a
number of formats.  At its heart is the the ```TypeEncoder``` interface and the ```TypeRegistry``` class for interacting 
with each encoder.  The ```TypeRegistry``` provides a means for interacting with similar groups of ```TypeEncoders```.

By default the following are provided:
- ```SimpleTypeEncoders``` - This class contains methods for generating String encoders for all the core java types in a 
consistent and easy serialize format. It could be used for something such as Json or other string data formats.
- ```LexiTypeEncoders``` - This class containts methods for generating lexicographically encoded Strings for all the 
core java types. It is best suited for situations where the order of the encoded strings needs to match the same natural 
ordering of the original data.  This is useful in situations like sorted key-value databases. 