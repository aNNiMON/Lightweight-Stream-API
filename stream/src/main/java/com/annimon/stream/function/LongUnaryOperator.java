package com.annimon.stream.function;

/**
 * Represents an operation on a single {@code long}-valued operand
 * that produces a {@code long}-valued result.
 *
 * @since 1.1.4
 * @see UnaryOperator
 */
public interface LongUnaryOperator {

    /**
     * Applies this operator to the given operand.
     *
     * @param operand the operand
     * @return the operator result
     */
    long applyAsLong(long operand);

    class Util {

        private Util() { }

        /**
         * Returns a unary operator that always returns its input argument.
         *
         * @return a unary operator that always returns its input argument
         */
        public static LongUnaryOperator identity() {
            return new LongUnaryOperator() {
                @Override
                public long applyAsLong(long operand) {
                    return operand;
                }
            };
        }
    }
}
