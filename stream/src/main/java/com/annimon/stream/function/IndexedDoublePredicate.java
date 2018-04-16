package com.annimon.stream.function;

import com.annimon.stream.Objects;

/**
 * Represents a predicate (function with boolean type result) with additional index argument.
 *
 * @since 1.2.1
 */
public interface IndexedDoublePredicate {

    /**
     * Tests the value for satisfying predicate.
     *
     * @param index  the index
     * @param value  the value to be tested
     * @return {@code true} if the value matches the predicate, otherwise {@code false}
     */
    boolean test(int index, double value);

    class Util {

        private Util() { }

        /**
         * Wraps {@link DoublePredicate} and returns {@code IndexedLongPredicate}.
         *
         * @param predicate  the predicate to wrap
         * @return a wrapped {@code IndexedDoublePredicate}
         * @throws NullPointerException if {@code predicate} is null
         */
        public static IndexedDoublePredicate wrap(final DoublePredicate predicate) {
            Objects.requireNonNull(predicate);
            return new IndexedDoublePredicate() {
                @Override
                public boolean test(int index, double value) {
                    return predicate.test(value);
                }
            };
        }

    }
}
