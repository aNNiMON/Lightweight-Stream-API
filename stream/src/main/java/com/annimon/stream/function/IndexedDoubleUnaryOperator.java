package com.annimon.stream.function;

import com.annimon.stream.Objects;

/**
 * Represents an operation on index and input {@code double}-valued operand
 * that produces a {@code double}-valued result.
 *
 * @since 1.2.1
 * @see DoubleUnaryOperator
 */
public interface IndexedDoubleUnaryOperator {

    /**
     * Applies this operator to the given operand.
     *
     * @param index  the index
     * @param operand  the operand
     * @return the operator result
     */
    double applyAsDouble(int index, double operand);

    class Util {

        private Util() { }

        /**
         * Wraps {@link DoubleUnaryOperator} and returns {@code IndexedDoubleUnaryOperator}.
         *
         * @param function  the function to wrap
         * @return a wrapped {@code IndexedDoubleUnaryOperator}
         * @throws NullPointerException if {@code function} is null
         */
        public static IndexedDoubleUnaryOperator wrap(
                final DoubleUnaryOperator function) {
            Objects.requireNonNull(function);
            return new IndexedDoubleUnaryOperator() {
                @Override
                public double applyAsDouble(int index, double value) {
                    return function.applyAsDouble(value);
                }
            };
        }
    }
}
