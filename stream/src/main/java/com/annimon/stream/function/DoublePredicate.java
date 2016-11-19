package com.annimon.stream.function;

/**
 * Represents a {@code double}-valued predicate (function with boolean type result).
 *
 * @since 1.1.4
 * @see Predicate
 */
@FunctionalInterface
public interface DoublePredicate {

    /**
     * Tests the value for satisfying predicate.
     *
     * @param value  the value to be tests
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
    }
}
