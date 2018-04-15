package com.annimon.stream.function;

/**
 * Represents an operation on input argument and can throw an exception.
 *
 * @param <T> the type of the input to the operation
 * @param <E> the type of the exception
 * @see Consumer
 */
public interface ThrowableConsumer<T, E extends Throwable> {

    /**
     * Performs operation on argument.
     *
     * @param value  the input argument
     * @throws E an exception
     */
    void accept(T value) throws E;
}
