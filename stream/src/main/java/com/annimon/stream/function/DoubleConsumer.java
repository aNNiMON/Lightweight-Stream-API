package com.annimon.stream.function;

/**
 * Represents an operation on a {@code double}-valued input argument.
 *
 * @since 1.1.4
 * @see Consumer
 */
@FunctionalInterface
public interface DoubleConsumer {

    /**
     * Performs operation on the given argument.
     *
     * @param value  the input argument
     */
    void accept(double value);
}
