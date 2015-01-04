package com.annimon.stream.function;

/**
 * Function with boolean type result.
 * 
 * @author aNNiMON
 */
public interface Predicate {
    
    boolean test(Object value);
    
    public static class Util {
        /**
         * Apply logical AND to predicates.
         */
        public static Predicate and(final Predicate p1, final Predicate p2) {
            return new Predicate() {
                public boolean test(Object value) {
                    return p1.test(value) && p2.test(value);
                }
            };
        }

        /**
         * Apply logical OR to predicates.
         */
        public static Predicate or(final Predicate p1, final Predicate p2) {
            return new Predicate() {
                public boolean test(Object value) {
                    return p1.test(value) || p2.test(value);
                }
            };
        }
        
        /**
         * Apply logical XOR to predicates.
         */
        public static Predicate xor(final Predicate p1, final Predicate p2) {
            return new Predicate() {
                public boolean test(Object value) {
                    return p1.test(value) ^ p2.test(value);
                }
            };
        }

        /**
         * Apply logical negation to predicate.
         */
        public static Predicate negate(final Predicate p1) {
            return new Predicate() {
                public boolean test(Object value) {
                    return !p1.test(value);
                }
            };
        }
    }
}
