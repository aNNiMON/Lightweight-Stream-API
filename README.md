Lightweight-Stream-API
======================

[![Join the chat at https://gitter.im/aNNiMON/Lightweight-Stream-API](https://badges.gitter.im/Join%20Chat.svg)](https://gitter.im/aNNiMON/Lightweight-Stream-API?utm_source=badge&utm_medium=badge&utm_campaign=pr-badge&utm_content=badge)

[![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.annimon/stream/badge.svg?style=flat)](https://maven-badges.herokuapp.com/maven-central/com.annimon/stream)
[![Build Status](https://travis-ci.org/aNNiMON/Lightweight-Stream-API.svg?branch=master)](https://travis-ci.org/aNNiMON/Lightweight-Stream-API)
[![Coverage Status](https://coveralls.io/repos/aNNiMON/Lightweight-Stream-API/badge.svg?branch=master&service=github)](https://coveralls.io/github/aNNiMON/Lightweight-Stream-API?branch=master)

Stream API from Java 8 rewrited on iterators for Java 7 and below.

Includes:
 + Functional interfaces;
 + `Stream` (without parallel processing);
 + `Optional` class;
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
Stream.ofRange(0, 10)...
```
Example project: https://github.com/aNNiMON/Android-Java-8-Stream-Example

### Download

Download [latest release](https://github.com/aNNiMON/Lightweight-Stream-API/releases) or grab via Maven:
```xml
<dependency>
  <groupId>com.annimon</groupId>
  <artifactId>stream</artifactId>
  <version>1.0.3</version>
</dependency>`
```
or Gradle:
```groovy
dependencies {
  ...
  compile 'com.annimon:stream:1.0.3'
  ...
}
```


Also included version for **Java ME**. Checkout [javame branch](https://github.com/aNNiMON/Lightweight-Stream-API/tree/javame).

For use lambda expressions in Java 6, Java 7 or Android, take look at [Retrolambda](https://github.com/orfjackal/retrolambda) repository.
