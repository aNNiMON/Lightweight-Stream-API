package com.annimon.stream.function;

import com.annimon.stream.Objects;

/**
 * Represents a predicate (function with boolean type result) with additional index argument.
 *
 * @param <T> the type of the input to the function
 * @since 1.1.6
 */
public interface IndexedPredicate<T> {

    /**
     * Tests the value for satisfying predicate.
     *
     * @param index  the index
     * @param value  the value to be tested
     * @return {@code true} if the value matches the predicate, otherwise {@code false}
     */
    boolean test(int index, T value);

    class Util {

        private Util() { }

        /**
         * Wraps {@link Predicate} and returns {@code IndexedPredicate}.
         *
         * @param <T> the type of the input to the function
         * @param predicate  the predicate to wrap
         * @return a wrapped {@code IndexedPredicate}
         * @throws NullPointerException if {@code predicate} is null
         */
        public static <T> IndexedPredicate<T> wrap(final Predicate<? super T> predicate) {
            Objects.requireNonNull(predicate);
            return new IndexedPredicate<T>() {
                @Override
                public boolean test(int index, T value) {
                    return predicate.test(value);
                }
            };
        }

    }
}
