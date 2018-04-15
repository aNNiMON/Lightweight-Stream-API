package com.annimon.stream.function;

/**
 * Represents an {@code int}-valued predicate (function with boolean type result).
 *
 * @param <E> the type of the exception
 * @since 1.1.7
 * @see IntPredicate
 */
public interface ThrowableIntPredicate<E extends Throwable> {

    /**
     * Tests the value for satisfying predicate.
     *
     * @param value  the value to be tested
     * @return {@code true} if the value matches the predicate, otherwise {@code false}
     * @throws E an exception
     */
    boolean test(int value) throws E;
}
