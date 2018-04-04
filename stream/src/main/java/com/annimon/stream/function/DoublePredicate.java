package com.annimon.stream.function;

/**
 * Represents a {@code double}-valued predicate (function with boolean type result).
 *
 * @since 1.1.4
 * @see Predicate
 */
public interface DoublePredicate {

    /**
     * Tests the value for satisfying predicate.
     *
     * @param value  the value to be tested
     * @return {@code true} if the value matches the predicate, otherwise {@code false}
     */
    boolean test(double value);

    class Util {

        private Util() { }

        /**
         * Applies logical AND to predicates.
         *
         * @param p1  the first predicate
         * @param p2  the second predicate
         * @return a composed {@code DoublePredicate}
         * @throws NullPointerException if {@code p1} or {@code p2} is null
         */
        public static DoublePredicate and(final DoublePredicate p1, final DoublePredicate p2) {
            return new DoublePredicate() {
                @Override
                public boolean test(double value) {
                    return p1.test(value) && p2.test(value);
                }
            };
        }

        /**
         * Applies logical OR to predicates.
         *
         * @param p1  the first predicate
         * @param p2  the second predicate
         * @return a composed {@code DoublePredicate}
         * @throws NullPointerException if {@code p1} or {@code p2} is null
         */
        public static DoublePredicate or(final DoublePredicate p1, final DoublePredicate p2) {
            return new DoublePredicate() {
                @Override
                public boolean test(double value) {
                    return p1.test(value) || p2.test(value);
                }
            };
        }

        /**
         * Applies logical XOR to predicates.
         *
         * @param p1  the first predicate
         * @param p2  the second predicate
         * @return a composed {@code DoublePredicate}
         * @throws NullPointerException if {@code p1} or {@code p2} is null
         */
        public static DoublePredicate xor(final DoublePredicate p1, final DoublePredicate p2) {
            return new DoublePredicate() {
                @Override
                public boolean test(double value) {
                    return p1.test(value) ^ p2.test(value);
                }
            };
        }

        /**
         * Applies logical negation to predicate.
         *
         * @param p1  the predicate to be negated
         * @return a composed {@code DoublePredicate}
         * @throws NullPointerException if {@code p1} is null
         */
        public static DoublePredicate negate(final DoublePredicate p1) {
            return new DoublePredicate() {
                @Override
                public boolean test(double value) {
                    return !p1.test(value);
                }
            };
        }

        /**
         * Creates a safe {@code DoublePredicate}.
         *
         * @param throwablePredicate  the predicate that may throw an exception
         * @return a {@code DoublePredicate} or {@code false} if exception was thrown
         * @since 1.1.7
         * @see #safe(com.annimon.stream.function.ThrowableDoublePredicate, boolean)
         */
        public static DoublePredicate safe(ThrowableDoublePredicate<Throwable> throwablePredicate) {
            return safe(throwablePredicate, false);
        }

        /**
         * Creates a safe {@code DoublePredicate}.
         *
         * @param throwablePredicate  the predicate that may throw an exception
         * @param resultIfFailed  the result which returned if exception was thrown
         * @return a {@code DoublePredicate} or {@code resultIfFailed}
         * @throws NullPointerException if {@code throwablePredicate} is null
         * @since 1.1.7
         */
        public static DoublePredicate safe(
                final ThrowableDoublePredicate<Throwable> throwablePredicate,
                final boolean resultIfFailed) {
            return new DoublePredicate() {

                @Override
                public boolean test(double value) {
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
