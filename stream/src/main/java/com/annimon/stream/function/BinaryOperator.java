package com.annimon.stream.function;

import com.annimon.stream.Objects;
import java.util.Comparator;

/**
 * Represents an operation on two operands that produces a result of the
 * same type as its operand.
 *
 * @param <T> the type of the operands and result of the operator
 */
public interface BinaryOperator<T> extends BiFunction<T, T, T> {
    class Util {

        private Util() { }

        /**
         * Returns a {@code BinaryOperator} which returns lesser of two elements
         * according to the specified {@code Comparator}.
         *
         * @param <T> the type of the input arguments of the comparator
         * @param comparator  a {@code Comparator} for comparing the two values
         * @return a {@code BinaryOperator} which returns the lesser of its operands,
         *         according to the supplied {@code Comparator}
         * @throws NullPointerException if the argument is null
         */
        public static <T> BinaryOperator<T> minBy(final Comparator<? super T> comparator) {
            Objects.requireNonNull(comparator);
            return new BinaryOperator<T>() {
                @Override
                public T apply(T a, T b) {
                    return comparator.compare(a, b) <= 0 ? a : b;
                }
            };
        }

        /**
         * Returns a {@code BinaryOperator} which returns greater of two elements
         * according to the specified {@code Comparator}.
         *
         * @param <T> the type of the input arguments of the comparator
         * @param comparator  a {@code Comparator} for comparing the two values
         * @return a {@code BinaryOperator} which returns the greater of its operands,
         *         according to the supplied {@code Comparator}
         * @throws NullPointerException if the argument is null
         */
        public static <T> BinaryOperator<T> maxBy(final Comparator<? super T> comparator) {
            Objects.requireNonNull(comparator);
            return new BinaryOperator<T>() {
                @Override
                public T apply(T a, T b) {
                    return comparator.compare(a, b) >= 0 ? a : b;
                }
            };
        }
    }
}
