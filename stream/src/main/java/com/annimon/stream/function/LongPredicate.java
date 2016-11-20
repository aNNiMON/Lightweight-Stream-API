package com.annimon.stream.function;

/**
 * Represents a {@code long}-valued predicate (function with boolean type result).
 *
 * @since 1.1.4
 * @see Predicate
 */
@FunctionalInterface
public interface LongPredicate {

    /**
     * Tests the value for satisfying predicate.
     *
     * @param value  the value to be tests
     * @return {@code true} if the value matches the predicate, otherwise {@code false}
     */
    boolean test(long value);

    class Util {

        private Util() { }

        /**
         * Applies logical AND to predicates.
         *
         * @param p1  the first predicate
         * @param p2  the second predicate
         * @return a composed {@code LongPredicate}
         * @throws NullPointerException if {@code p1} or {@code p2} is null
         */
        public static LongPredicate and(final LongPredicate p1, final LongPredicate p2) {
            return new LongPredicate() {
                @Override
                public boolean test(long value) {
                    return p1.test(value) && p2.test(value);
                }
            };
        }

        /**
         * Applies logical OR to predicates.
         *
         * @param p1  the first predicate
         * @param p2  the second predicate
         * @return a composed {@code LongPredicate}
         * @throws NullPointerException if {@code p1} or {@code p2} is null
         */
        public static LongPredicate or(final LongPredicate p1, final LongPredicate p2) {
            return new LongPredicate() {
                @Override
                public boolean test(long value) {
                    return p1.test(value) || p2.test(value);
                }
            };
        }

        /**
         * Applies logical XOR to predicates.
         *
         * @param p1  the first predicate
         * @param p2  the second predicate
         * @return a composed {@code LongPredicate}
         * @throws NullPointerException if {@code p1} or {@code p2} is null
         */
        public static LongPredicate xor(final LongPredicate p1, final LongPredicate p2) {
            return new LongPredicate() {
                @Override
                public boolean test(long value) {
                    return p1.test(value) ^ p2.test(value);
                }
            };
        }

        /**
         * Applies logical negation to predicate.
         *
         * @param p1  the predicate to be negated
         * @return a composed {@code LongPredicate}
         * @throws NullPointerException if {@code p1} is null
         */
        public static LongPredicate negate(final LongPredicate p1) {
            return new LongPredicate() {
                @Override
                public boolean test(long value) {
                    return !p1.test(value);
                }
            };
        }
    }
}
