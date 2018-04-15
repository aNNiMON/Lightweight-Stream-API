package com.annimon.stream.function;

/**
 * Represents an operation on two input arguments.
 *
 * @param <T> the type of the first argument
 * @since 1.1.4
 * @see BiConsumer
 */
public interface ObjDoubleConsumer<T> {

    /**
     * Performs operation on two arguments.
     *
     * @param t  the first argument
     * @param value  the second argument
     */
    void accept(T t, double value);
}
