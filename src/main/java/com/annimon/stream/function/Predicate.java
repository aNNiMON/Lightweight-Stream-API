package com.annimon.stream.function;

/**
 * Represents a predicate, i.e. function with boolean type result.
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
         * Apply logical AND to predicates.
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
         * Apply logical OR to predicates.
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
         * Apply logical XOR to predicates.
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
         * Apply logical negation to predicate.
         * 
         * @param <T> the type of the input to the function
         * @param p1  the predicate to be negated
         * @return a composed {@code Predicate}
         * @throws NullPointerException if {@code p1} is null
         */
        public static <T> Predicate<? super T> negate(final Predicate<? super T> p1) {
            return new Predicate<T>() {
                @Override
                public boolean test(T value) {
                    return !p1.test(value);
                }
            };
        }
    }
}
