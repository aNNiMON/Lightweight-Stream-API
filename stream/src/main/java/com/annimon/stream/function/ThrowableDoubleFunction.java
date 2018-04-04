package com.annimon.stream.function;

/**
 * Represents a function which produces result from {@code double}-valued input argument.
 *
 * @param <R> the type of the result of the function
 * @param <E> the type of the exception
 *
 * @since 1.1.7
 * @see DoubleFunction
 */
public interface ThrowableDoubleFunction<R, E extends Throwable> {

    /**
     * Applies this function to the given argument.
     *
     * @param value  the function argument
     * @return the function result
     * @throws E an exception
     */
    R apply(double value) throws E;
}