package com.annimon.stream.function;

/**
 * Represents a function which supply a result.
 *
 * @param <T> the type of the result
 */
@FunctionalInterface
public interface Supplier<T> {

    /**
     * Gets a result.
     *
     * @return a result
     */
    T get();
}
