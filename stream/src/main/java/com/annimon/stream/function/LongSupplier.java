package com.annimon.stream.function;

/**
 * Represents a supplier of {@code long}-valued results.
 *
 * @since 1.1.4
 * @see Supplier
 */
@FunctionalInterface
public interface LongSupplier {

    /**
     * Gets a result.
     *
     * @return a result
     */
    long getAsLong();
}
