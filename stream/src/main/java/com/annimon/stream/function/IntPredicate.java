package com.annimon.stream.function;

import com.annimon.stream.Objects;
import org.jetbrains.annotations.NotNull;

/**
 * Represents a predicate (function with boolean type result).
 */
public interface IntPredicate {

    /**
     * Tests the value for satisfying predicate.
     *
     * @param value  the value to be tested
     * @return {@code true} if the value matches the predicate, otherwise {@code false}
     */
    boolean test(int value);

    class Util {

        private Util() { }

        /**
         * Applies logical AND to predicates.
         *
         * @param p1  the first predicate
         * @param p2  the second predicate
         * @return a composed {@code IntPredicate}
         * @throws NullPointerException if {@code p1} or {@code p2} is null
         */
        public static IntPredicate and(
                @NotNull final IntPredicate p1,
                @NotNull final IntPredicate p2) {
            Objects.requireNonNull(p1, "predicate1");
            Objects.requireNonNull(p2, "predicate2");
            return new IntPredicate() {
                @Override
                public boolean test(int value) {
                    return p1.test(value) && p2.test(value);
                }
            };
        }

        /**
         * Applies logical OR to predicates.
         *
         * @param p1  the first predicate
         * @param p2  the second predicate
         * @return a composed {@code IntPredicate}
         * @throws NullPointerException if {@code p1} or {@code p2} is null
         */
        public static IntPredicate or(
                @NotNull final IntPredicate p1,
                @NotNull final IntPredicate p2) {
            Objects.requireNonNull(p1, "predicate1");
            Objects.requireNonNull(p2, "predicate2");
            return new IntPredicate() {
                @Override
                public boolean test(int value) {
                    return p1.test(value) || p2.test(value);
                }
            };
        }

        /**
         * Applies logical XOR to predicates.
         *
         * @param p1  the first predicate
         * @param p2  the second predicate
         * @return a composed {@code IntPredicate}
         * @throws NullPointerException if {@code p1} or {@code p2} is null
         */
        public static IntPredicate xor(
                @NotNull final IntPredicate p1,
                @NotNull final IntPredicate p2) {
            Objects.requireNonNull(p1, "predicate1");
            Objects.requireNonNull(p2, "predicate2");
            return new IntPredicate() {
                @Override
                public boolean test(int value) {
                    return p1.test(value) ^ p2.test(value);
                }
            };
        }

        /**
         * Applies logical negation to predicate.
         *
         * @param p1  the predicate to be negated
         * @return a composed {@code IntPredicate}
         * @throws NullPointerException if {@code p1} is null
         */
        public static IntPredicate negate(@NotNull final IntPredicate p1) {
            Objects.requireNonNull(p1);
            return new IntPredicate() {
                @Override
                public boolean test(int value) {
                    return !p1.test(value);
                }
            };
        }

        /**
         * Creates a safe {@code IntPredicate}.
         *
         * @param throwablePredicate  the predicate that may throw an exception
         * @return an {@code IntPredicate} or {@code false} if exception was thrown
         * @since 1.1.7
         * @see #safe(com.annimon.stream.function.ThrowableIntPredicate, boolean)
         */
        public static IntPredicate safe(@NotNull ThrowableIntPredicate<Throwable> throwablePredicate) {
            return safe(throwablePredicate, false);
        }

        /**
         * Creates a safe {@code IntPredicate}.
         *
         * @param throwablePredicate  the predicate that may throw an exception
         * @param resultIfFailed  the result which returned if exception was thrown
         * @return an {@code IntPredicate} or {@code resultIfFailed}
         * @throws NullPointerException if {@code throwablePredicate} is null
         * @since 1.1.7
         */
        public static IntPredicate safe(
                @NotNull final ThrowableIntPredicate<Throwable> throwablePredicate,
                final boolean resultIfFailed) {
            Objects.requireNonNull(throwablePredicate);
            return new IntPredicate() {

                @Override
                public boolean test(int value) {
                    try {
                        return throwablePredicate.test(value);
                    } catch (Throwable throwable) {
                        return resultIfFailed;
                    }
                }
            };
        }

    }
}
