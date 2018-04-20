package com.annimon.stream.function;

import com.annimon.stream.Objects;

/**
 * Represents a predicate (function with boolean type result) with additional index argument.
 *
 * @since 1.2.1
 */
public interface IndexedLongPredicate {

    /**
     * Tests the value for satisfying predicate.
     *
     * @param index  the index
     * @param value  the value to be tested
     * @return {@code true} if the value matches the predicate, otherwise {@code false}
     */
    boolean test(int index, long value);

    class Util {

        private Util() { }

        /**
         * Wraps {@link LongPredicate} and returns {@code IndexedLongPredicate}.
         *
         * @param predicate  the predicate to wrap
         * @return a wrapped {@code IndexedLongPredicate}
         * @throws NullPointerException if {@code predicate} is null
         */
        public static IndexedLongPredicate wrap(final LongPredicate predicate) {
            Objects.requireNonNull(predicate);
            return new IndexedLongPredicate() {
                @Override
                public boolean test(int index, long value) {
                    return predicate.test(value);
                }
            };
        }

    }
}
