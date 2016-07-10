package com.annimon.stream.function;

/**
 * Represents a predicate, i.e. function with boolean type result.
 *
 */
@FunctionalInterface
public interface IntPredicate {

    /**
     * Tests the value for satisfying predicate.
     *
     * @param value  the value to be tests
     * @return {@code true} if the value matches the predicate, otherwise {@code false}
     */
    boolean test(int value);

    class Util {

        private Util() {
            throw new UnsupportedOperationException();
        }

        /**
         * Applies logical AND to predicates.
         *
         * @param p1  the first predicate
         * @param p2  the second predicate
         * @return a composed {@code Predicate}
         * @throws NullPointerException if {@code p1} or {@code p2} is null
         */
        public static IntPredicate and(final IntPredicate p1, final IntPredicate p2) {
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
         * @return a composed {@code Predicate}
         * @throws NullPointerException if {@code p1} or {@code p2} is null
         */
        public static IntPredicate or(final IntPredicate p1, final IntPredicate p2) {
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
         * @return a composed {@code Predicate}
         * @throws NullPointerException if {@code p1} or {@code p2} is null
         */
        public static IntPredicate xor(final IntPredicate p1, final IntPredicate p2) {
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
         * @return a composed {@code Predicate}
         * @throws NullPointerException if {@code p1} is null
         */
        public static IntPredicate negate(final IntPredicate p1) {
            return new IntPredicate() {
                @Override
                public boolean test(int value) {
                    return !p1.test(value);
                }
            };
        }
    }
}
