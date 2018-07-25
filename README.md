Lightweight-Stream-API
======================

[![Join the chat at https://gitter.im/aNNiMON/Lightweight-Stream-API](https://badges.gitter.im/Join%20Chat.svg)](https://gitter.im/aNNiMON/Lightweight-Stream-API?utm_source=badge&utm_medium=badge&utm_campaign=pr-badge&utm_content=badge)
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.annimon/stream/badge.svg?style=flat)](https://maven-badges.herokuapp.com/maven-central/com.annimon/stream)
[![Build Status](https://travis-ci.org/aNNiMON/Lightweight-Stream-API.svg?branch=master)](https://travis-ci.org/aNNiMON/Lightweight-Stream-API)
[![Coverage Status](https://coveralls.io/repos/aNNiMON/Lightweight-Stream-API/badge.svg?branch=master&service=github)](https://coveralls.io/github/aNNiMON/Lightweight-Stream-API?branch=master)
[![](http://javadoc-badge.appspot.com/com.annimon/stream.svg?label=JavaDocs)](http://www.javadoc.io/doc/com.annimon/stream/)

Stream API from Java 8 rewritten on iterators for Java 7 and below.


### Includes

 + Functional interfaces (`Supplier`, `Function`, `Consumer` etc);
 + `Stream`/`IntStream`/`LongStream`/`DoubleStream` (without parallel processing, but with a variety of additional methods and with custom operators);
 + `Optional`/`OptionalBoolean`/`OptionalInt`/`OptionalLong`/`OptionalDouble` classes;
 + `Exceptional` class - functional way to deal with exceptions;
 + `Objects` from Java 7.


### Usage

```java
Stream.of(/* array | list | set | map | anything based on Iterator/Iterable interface */)
    .filter(..)
    .map(..)
    ...
    .sorted()
    .forEach(..);
Stream.of(value1, value2, value3)...
IntStream.range(0, 10)...
```
Example project: https://github.com/aNNiMON/Android-Java-8-Stream-Example


## Key features

### Custom operators

Unlike Java 8 streams, Lightweight-Stream-API provides the ability to apply custom operators.

```java
Stream.of(...)
    .custom(new Reverse<>())
    .forEach(...);

public final class Reverse<T> implements UnaryOperator<Stream<T>> {

    @Override
    public Stream<T> apply(Stream<T> stream) {
        final Iterator<? extends T> iterator = stream.getIterator();
        final ArrayDeque<T> deque = new ArrayDeque<T>();
        while (iterator.hasNext()) {
            deque.addFirst(iterator.next());
        }
        return Stream.of(deque.iterator());
    }
}
```

You can find more examples [here](https://github.com/aNNiMON/Lightweight-Stream-API/blob/master/stream/src/test/java/com/annimon/stream/CustomOperators.java).

### Additional operators

In addition to backported Java 8 Stream operators, the library provides:

- `filterNot` - negated `filter` operator

  ```java
  // Java 8
  stream.filter(((Predicate<String>) String::isEmpty).negate())
  // LSA
  stream.filterNot(String::isEmpty)
  ```

- `select` - filters instances of the given class

  ```java
  // Java 8
  stream.filter(Integer.class::isInstance)
  // LSA
  stream.select(Integer.class)
  ```

- `withoutNulls` - filters only not null elements

  ```java
  Stream.of("a", null, "c", "d", null)
      .withoutNulls() // [a, c, d]
  ```

- `sortBy` - sorts by extractor function

  ```java
  // Java 8
  stream.sorted(Comparator.comparing(Person::getName))
  // LSA
  stream.sortBy(Person::getName)
  ```

- `groupBy` - groups by extractor function

  ```java
  // Java 8
  stream.collect(Collectors.groupingBy(Person::getName)).entrySet().stream()
  // LSA
  stream.groupBy(Person::getName)
  ```

- `chunkBy` - partitions sorted stream by classifier function

  ```java
  Stream.of("a", "b", "cd", "ef", "gh", "ij", "klmnn")
      .chunkBy(String::length) // [[a, b], [cd, ef, gh, ij], [klmnn]]
  ```

- `sample` - emits every n-th elements

  ```java
  Stream.rangeClosed(0, 10)
      .sample(2) // [0, 2, 4, 6, 8, 10]
  ```

- `slidingWindow` - partitions stream into fixed-sized list and sliding over the elements

  ```java
  Stream.rangeClosed(0, 10)
      .slidingWindow(4, 6) // [[0, 1, 2, 3], [6, 7, 8, 9]]
  ```

- `takeWhile` / `dropWhile` - introduced in Java 9, limits/skips stream by predicate function

  ```java
  Stream.of("a", "b", "cd", "ef", "g")
      .takeWhile(s -> s.length() == 1) // [a, b]
  Stream.of("a", "b", "cd", "ef", "g")
      .dropWhile(s -> s.length() == 1) // [cd, ef, g]
  ```

- `scan` - iteratively applies accumulation function and returns Stream

  ```java
  IntStream.range(1, 6)
      .scan((a, b) -> a + b) // [1, 3, 6, 10, 15]
  ```

- `indexed` - adds an index to every element, result is `IntPair`

  ```java
  Stream.of("a", "b", "c")
      .indexed() // [(0 : "a"), (1 : "b"), (2 : "c")]
  ```

- `filterIndexed` / `mapIndexed` / `takeWhileIndexed` / `takeUntilIndexed` / `dropWhileIndexed` / `reduceIndexed` / `forEachIndexed` - indexed specialization of operators

  ```java
  Stream.of("a", "b", "c")
      .mapIndexed((i, s) -> s + Integer.toString(i)) // [a0, b1, c2]
  ```


### Throwable functions

No more ugly try/catch in lambda expressions.

```java
// Java 8
stream.map(file -> {
    try {
        return new FileInputStream(file);
    } catch (IOException ioe) {
        return null;
    }
})
// LSA
stream.map(Function.Util.safe(FileInputStream::new))
```


## Download

Download [latest release](https://github.com/aNNiMON/Lightweight-Stream-API/releases) or grab via Maven:

```xml
<dependency>
  <groupId>com.annimon</groupId>
  <artifactId>stream</artifactId>
  <version>1.2.1</version>
</dependency>
```
or Gradle:

```groovy
dependencies {
  ...
  implementation 'com.annimon:stream:1.2.1'
  ...
}
```

or use latest unrealeased features with [JitPack](https://jitpack.io/#aNNiMON/Lightweight-Stream-API).

Also included version for **Java ME**. Checkout [javame branch](https://github.com/aNNiMON/Lightweight-Stream-API/tree/javame).
