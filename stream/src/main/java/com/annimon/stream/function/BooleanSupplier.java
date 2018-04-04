package com.annimon.stream.function;

/**
 * Represents a supplier of {@code boolean}-valued results.
 *
 * @since 1.1.8
 * @see Supplier
 */
public interface BooleanSupplier {

    /**
     * Gets a result.
     *
     * @return a result
     */
    boolean getAsBoolean();
}
