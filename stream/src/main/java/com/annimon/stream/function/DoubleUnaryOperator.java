package com.annimon.stream.function;

/**
 * Represents an operation on a single {@code double}-valued operand
 * that produces a {@code double}-valued result.
 *
 * @since 1.1.4
 * @see UnaryOperator
 */
public interface DoubleUnaryOperator {

    /**
     * Applies this operator to the given operand.
     *
     * @param operand the operand
     * @return the operator result
     */
    double applyAsDouble(double operand);

    class Util {

        private Util() { }

        /**
         * Returns a unary operator that always returns its input argument.
         *
         * @return a unary operator that always returns its input argument
         */
        public static DoubleUnaryOperator identity() {
            return new DoubleUnaryOperator() {
                @Override
                public double applyAsDouble(double operand) {
                    return operand;
                }
            };
        }
    }
}
