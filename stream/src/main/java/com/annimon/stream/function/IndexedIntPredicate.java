package com.annimon.stream.function;

import com.annimon.stream.Objects;

/**
 * Represents a predicate (function with boolean type result) with additional index argument.
 *
 * @since 1.2.1
 */
public interface IndexedIntPredicate {

    /**
     * Tests the value for satisfying predicate.
     *
     * @param index  the index
     * @param value  the value to be tested
     * @return {@code true} if the value matches the predicate, otherwise {@code false}
     */
    boolean test(int index, int value);

    class Util {

        private Util() { }

        /**
         * Wraps {@link IntPredicate} and returns {@code IndexedIntPredicate}.
         *
         * @param predicate  the predicate to wrap
         * @return a wrapped {@code IndexedIntPredicate}
         * @throws NullPointerException if {@code predicate} is null
         */
        public static IndexedIntPredicate wrap(final IntPredicate predicate) {
            Objects.requireNonNull(predicate);
            return new IndexedIntPredicate() {
                @Override
                public boolean test(int index, int value) {
                    return predicate.test(value);
                }
            };
        }

    }
}
