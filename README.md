Lightweight-Stream-API
======================

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
  <version>1.0.2</version>
</dependency>`
```
or Gradle:
```groovy
dependencies {
  ...
  compile 'com.annimon:stream:1.0.2'
  ...
}
```


Also included version for **Java ME**. Checkout [javame branch](https://github.com/aNNiMON/Lightweight-Stream-API/tree/javame).

For use lambda expressions in Java 6, Java 7 or Android, take look at [Retrolambda](https://github.com/orfjackal/retrolambda) repository.
