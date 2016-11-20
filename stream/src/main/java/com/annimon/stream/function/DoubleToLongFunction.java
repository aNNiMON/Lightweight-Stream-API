package com.annimon.stream.function;

/**
 * Represents a function which produces an {@code long}-valued result from input argument.
 *
 * @since 1.1.4
 * @see Function
 */
public interface DoubleToLongFunction {

    /**
     * Applies this function to the given argument.
     *
     * @param value  an argument
     * @return the function result
     */
    long applyAsLong(double value);
}
