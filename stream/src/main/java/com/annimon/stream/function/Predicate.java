package com.annimon.stream.function;

/**
 * Represents a predicate (function with boolean type result).
 *
 * @param <T> the type of the input to the function
 */
@FunctionalInterface
public interface Predicate<T> {

    /**
     * Tests the value for satisfying predicate.
     *
     * @param value  the value to be tests
     * @return {@code true} if the value matches the predicate, otherwise {@code false}
     */
    boolean test(T value);

    class Util {

        private Util() { }

        /**
         * Applies logical AND to predicates.
         *
         * @param <T> the type of the input to the function
         * @param p1  the first predicate
         * @param p2  the second predicate
         * @return a composed {@code Predicate}
         * @throws NullPointerException if {@code p1} or {@code p2} is null
         */
        public static <T> Predicate<T> and(final Predicate<? super T> p1, final Predicate<? super T> p2) {
            return new Predicate<T>() {
                @Override
                public boolean test(T value) {
                    return p1.test(value) && p2.test(value);
                }
            };
        }

        /**
         * Applies logical OR to predicates.
         *
         * @param <T> the type of the input to the function
         * @param p1  the first predicate
         * @param p2  the second predicate
         * @return a composed {@code Predicate}
         * @throws NullPointerException if {@code p1} or {@code p2} is null
         */
        public static <T> Predicate<T> or(final Predicate<? super T> p1, final Predicate<? super T> p2) {
            return new Predicate<T>() {
                @Override
                public boolean test(T value) {
                    return p1.test(value) || p2.test(value);
                }
            };
        }

        /**
         * Applies logical XOR to predicates.
         *
         * @param <T> the type of the input to the function
         * @param p1  the first predicate
         * @param p2  the second predicate
         * @return a composed {@code Predicate}
         * @throws NullPointerException if {@code p1} or {@code p2} is null
         */
        public static <T> Predicate<T> xor(final Predicate<? super T> p1, final Predicate<? super T> p2) {
            return new Predicate<T>() {
                @Override
                public boolean test(T value) {
                    return p1.test(value) ^ p2.test(value);
                }
            };
        }

        /**
         * Applies logical negation to predicate.
         *
         * @param <T> the type of the input to the function
         * @param p1  the predicate to be negated
         * @return a composed {@code Predicate}
         * @throws NullPointerException if {@code p1} is null
         */
        public static <T> Predicate<T> negate(final Predicate<? super T> p1) {
            return new Predicate<T>() {
                @Override
                public boolean test(T value) {
                    return !p1.test(value);
                }
            };
        }

        /**
         * Creates a safe {@code Predicate}.
         *
         * @param <T> the type of the input to the function
         * @param throwablePredicate  the predicate that may throw an exception
         * @return a {@code Predicate} or {@code false} if exception was thrown
         */
        public static <T> Predicate<T> safe(ThrowablePredicate<? super T, Throwable> throwablePredicate) {
            return safe(throwablePredicate, false);
        }

        /**
         * Creates a safe {@code Predicate}.
         *
         * @param <T> the type of the input to the function
         * @param throwablePredicate  the predicate that may throw an exception
         * @param resultIfFailed  the result which returned if exception was thrown
         * @return a {@code Predicate} or {@code resultIfFailed}
         * @throws NullPointerException if {@code throwablePredicate} is null
         */
        public static <T> Predicate<T> safe(
                final ThrowablePredicate<? super T, Throwable> throwablePredicate,
                final boolean resultIfFailed) {
            return new Predicate<T>() {

                @Override
                public boolean test(T value) {
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
