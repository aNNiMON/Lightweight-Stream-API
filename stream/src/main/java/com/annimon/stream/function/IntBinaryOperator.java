package com.annimon.stream.function;

/**
 * Represents an operation upon two {@code int}-valued operands and producing an
 * {@code int}-valued result.  This is the primitive type specialization of
 * {@link com.annimon.stream.function.BinaryOperator} for {@code int}.
 *
 * <p>This is a functional interface whose functional method is {@link #applyAsInt(int, int)}.
 *
 * @see com.annimon.stream.function.BinaryOperator
 * @see IntUnaryOperator
 */
public interface IntBinaryOperator {

    /**
     * Applies this operator to the given operands.
     *
     * @param left the first operand
     * @param right the second operand
     * @return the operator result
     */
    int applyAsInt(int left, int right);
}
