package com.annimon.stream.function;

/**
 * Represents a supplier of {@code double}-valued results.
 *
 * @param <E> the type of the exception
 * @since 1.1.7
 * @see DoubleSupplier
 */
public interface ThrowableDoubleSupplier<E extends Throwable> {

    /**
     * Gets a result.
     *
     * @return a result
     * @throws E an exception
     */
    double getAsDouble() throws E;
}
