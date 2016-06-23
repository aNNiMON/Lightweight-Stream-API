package com.annimon.stream.function;

/**
 * Represents a predicate, i.e. function with boolean type result which can throw an exception.
 *
 * @param <T> the type of the input to the function
 * @param <E> the type of the exception
 */
@FunctionalInterface
public interface ThrowablePredicate<T, E extends Throwable> {

    /**
     * Tests the value for satisfying predicate.
     *
     * @param value  the value to be tests
     * @return {@code true} if the value matches the predicate, otherwise {@code false}
     * @throws E an exception
     */
    boolean test(T value) throws E;
}
