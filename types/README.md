# Mango Types

When working with lexicographically sorted key/value stores (such as HBase and Accumulo), it's often necessary to standardize on a system for normalizing typed data so that it can be easily searched. This is one such implementation. The purpose of this type system is for maximum reusability & extensibility. We've managed to use it several projects and hope it'll be useful in yours.

This type system allows me to specify how to normalize a Java type to a lexicographically sorted string, and further denormalize back to a native Java type. Many standard Java types are already supported: Boolean, Double, Long, String, Byte, etc... As we'll cover below, new types can be plugged in very easily. Further, the type allows pretty-print versions to be specified so that it can cross language barriers as well as aid in client-side display.

# Example

Let's dive right in. Suppose I have a Double value that I'd like to normalize into a lexicographically sorted string. I can do so with the following:
```java
TypeNormalizer normalizer = new DoubleNormalizer();
Double myDouble = 45.56;
String normalized = normalizer.normalize(myDouble);
Double denormalized = normalizer.denormalize(normalized);
System.out.println(String.format("[normalized=%s, denormalized=%f]", normalized, denormalized);
```

Here, you are seeing how we invoke a TypeNormalizer manually. When types are registered with the TypeContext, however, you don't need to manually invoke a normalizer. Here's an example:
```java
Double myDouble = 45.56;
String normalized = TypeContext.normalize(myDouble);
Double denormalized = TypeContext.denormalize(normalized);
System.out.println(String.format("[normalized=%s, denormalized=%f]", normalized, denormalized);
```
