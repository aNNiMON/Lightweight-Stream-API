package com.annimon.stream.function;

/**
 * Represents a function which produces result from {@code long}-valued input argument.
 *
 * @param <R> the type of the result of the function
 *
 * @since 1.1.4
 * @see Function
 */
@FunctionalInterface
public interface LongFunction<R> {

    /**
     * Applies this function to the given argument.
     *
     * @param value  an argument
     * @return the function result
     */
    R apply(long value);
}