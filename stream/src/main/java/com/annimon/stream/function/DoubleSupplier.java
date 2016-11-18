package com.annimon.stream.function;

/**
 * Represents a supplier of {@code double}-valued results.
 *
 * @since 1.1.4
 * @see Supplier
 */
@FunctionalInterface
public interface DoubleSupplier {

    /**
     * Gets a result.
     *
     * @return a result
     */
    double getAsDouble();
}
