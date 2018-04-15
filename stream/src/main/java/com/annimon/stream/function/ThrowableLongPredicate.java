package com.annimon.stream.function;

/**
 * Represents a {@code long}-valued predicate (function with boolean type result).
 *
 * @param <E> the type of the exception
 * @since 1.1.7
 * @see LongPredicate
 */
public interface ThrowableLongPredicate<E extends Throwable> {

    /**
     * Tests the value for satisfying predicate.
     *
     * @param value  the value to be tested
     * @return {@code true} if the value matches the predicate, otherwise {@code false}
     * @throws E an exception
     */
    boolean test(long value) throws E;
}
