package com.annimon.stream.function;

/**
 * Represents an operation on a {@code long}-valued input argument.
 *
 * @param <E> the type of the exception
 * @since 1.1.7
 * @see LongConsumer
 */
public interface ThrowableLongConsumer<E extends Throwable> {

    /**
     * Performs operation on the given argument.
     *
     * @param value  the input argument
     * @throws E an exception
     */
    void accept(long value) throws E;
}
