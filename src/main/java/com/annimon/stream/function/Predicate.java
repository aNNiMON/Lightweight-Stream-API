package com.annimon.stream.function;

/**
 * Function with boolean type result.
 * 
 * @author aNNiMON
 * @param <T> the type of the input to the function
 */
@FunctionalInterface
public interface Predicate<T> {
    
    boolean test(T value);
    
    public static class Util {
        /**
         * Apply logical AND to predicates.
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
