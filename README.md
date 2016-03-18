Lightweight-Stream-API
======================

[![Join the chat at https://gitter.im/aNNiMON/Lightweight-Stream-API](https://badges.gitter.im/Join%20Chat.svg)](https://gitter.im/aNNiMON/Lightweight-Stream-API?utm_source=badge&utm_medium=badge&utm_campaign=pr-badge&utm_content=badge)

[![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.annimon/stream/badge.svg?style=flat)](https://maven-badges.herokuapp.com/maven-central/com.annimon/stream)
[![Build Status](https://travis-ci.org/aNNiMON/Lightweight-Stream-API.svg?branch=master)](https://travis-ci.org/aNNiMON/Lightweight-Stream-API)
[![Coverage Status](https://coveralls.io/repos/aNNiMON/Lightweight-Stream-API/badge.svg?branch=master&service=github)](https://coveralls.io/github/aNNiMON/Lightweight-Stream-API?branch=master)

Stream API from Java 8 rewrited on iterators for Java 7 and below.


### Includes

 + Functional interfaces (Supplier, Function, Consumer etc);
 + `Stream` (without parallel processing but with custom operators);
 + `Optional` class;
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
Stream.range(0, 10)...
```
Example project: https://github.com/aNNiMON/Android-Java-8-Stream-Example


### Custom operators

Unlike Java 8 streams, Lightweight-Stream-API provides an ability to apply custom operators.

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

More examples you can find [here](https://github.com/aNNiMON/Lightweight-Stream-API/blob/master/stream/src/test/java/com/annimon/stream/CustomOperators.java).


### Download

Download [latest release](https://github.com/aNNiMON/Lightweight-Stream-API/releases) or grab via Maven:
```xml
<dependency>
  <groupId>com.annimon</groupId>
  <artifactId>stream</artifactId>
  <version>1.0.8</version>
</dependency>
```
or Gradle:
```groovy
dependencies {
  ...
  compile 'com.annimon:stream:1.0.8'
  ...
}
```


Also included version for **Java ME**. Checkout [javame branch](https://github.com/aNNiMON/Lightweight-Stream-API/tree/javame).

For use lambda expressions in Java 6, Java 7 or Android, take look at [Retrolambda](https://github.com/orfjackal/retrolambda) repository.
