package com.annimon.stream.function;

/**
 * Represents an operation on two {@code long}-valued operands
 * that produces a {@code long}-valued result.
 *
 * @since 1.1.4
 * @see BinaryOperator
 */
public interface LongBinaryOperator {

    /**
     * Applies this operator to the given operands.
     *
     * @param left  the first operand
     * @param right  the second operand
     * @return the operator result
     */
    long applyAsLong(long left, long right);
}
