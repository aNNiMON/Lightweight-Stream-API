package com.annimon.stream.function;

/**
 * Represents a function which produces an {@code double}-valued result from input argument.
 *
 * @param <T> the type of the input of the function
 * @since 1.1.3
 * @see Function
 */
public interface ToDoubleFunction<T> {

    /**
     * Applies this function to the given argument.
     *
     * @param t  an argument
     * @return the function result
     */
    double applyAsDouble(T t);
}
