package com.annimon.stream.function;

/**
 * Represents a {@code boolean}-valued predicate (function with boolean type result).
 *
 * @since 1.1.8
 * @see Predicate
 * @see UnaryOperator
 */
public interface BooleanPredicate {

    /**
     * Tests the value for satisfying predicate.
     *
     * @param value  the value to be tested
     * @return {@code true} if the value matches the predicate, otherwise {@code false}
     */
    boolean test(boolean value);

    class Util {

        private Util() { }

        /**
         * Returns a predicate that always returns its input argument.
         *
         * @return a predicate that always returns its input argument
         */
        public static BooleanPredicate identity() {
            return new BooleanPredicate() {
                @Override
                public boolean test(boolean operand) {
                    return operand;
                }
            };
        }

        /**
         * Applies logical AND to predicates.
         *
         * @param p1  the first predicate
         * @param p2  the second predicate
         * @return a composed {@code BooleanPredicate}
         * @throws NullPointerException if {@code p1} or {@code p2} is null
         */
        public static BooleanPredicate and(final BooleanPredicate p1, final BooleanPredicate p2) {
            return new BooleanPredicate() {
                @Override
                public boolean test(boolean value) {
                    return p1.test(value) && p2.test(value);
                }
            };
        }

        /**
         * Applies logical OR to predicates.
         *
         * @param p1  the first predicate
         * @param p2  the second predicate
         * @return a composed {@code BooleanPredicate}
         * @throws NullPointerException if {@code p1} or {@code p2} is null
         */
        public static BooleanPredicate or(final BooleanPredicate p1, final BooleanPredicate p2) {
            return new BooleanPredicate() {
                @Override
                public boolean test(boolean value) {
                    return p1.test(value) || p2.test(value);
                }
            };
        }

        /**
         * Applies logical XOR to predicates.
         *
         * @param p1  the first predicate
         * @param p2  the second predicate
         * @return a composed {@code BooleanPredicate}
         * @throws NullPointerException if {@code p1} or {@code p2} is null
         */
        public static BooleanPredicate xor(final BooleanPredicate p1, final BooleanPredicate p2) {
            return new BooleanPredicate() {
                @Override
                public boolean test(boolean value) {
                    return p1.test(value) ^ p2.test(value);
                }
            };
        }

        /**
         * Applies logical negation to predicate.
         *
         * @param p1  the predicate to be negated
         * @return a composed {@code BooleanPredicate}
         * @throws NullPointerException if {@code p1} is null
         */
        public static BooleanPredicate negate(final BooleanPredicate p1) {
            return new BooleanPredicate() {
                @Override
                public boolean test(boolean value) {
                    return !p1.test(value);
                }
            };
        }

    }
}
