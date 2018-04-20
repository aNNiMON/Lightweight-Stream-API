package com.annimon.stream.function;

import com.annimon.stream.Objects;

/**
 * Represents a function which produces result from index and input argument.
 *
 * @param <R> the type of the result of the function
 * @since 1.2.1
 */
public interface IndexedLongFunction<R> {

    /**
     * Applies this function to the given argument.
     *
     * @param index  the index
     * @param value  an argument
     * @return the function result
     */
    R apply(int index, long value);

    class Util {

        private Util() { }

        /**
         * Wraps {@link LongFunction} and returns {@code IndexedLongFunction}.
         *
         * @param <R> the type of the result
         * @param function  the function to wrap
         * @return a wrapped {@code IndexedLongFunction}
         * @throws NullPointerException if {@code function} is null
         */
        public static <R> IndexedLongFunction<R> wrap(
                final LongFunction<? extends R> function) {
            Objects.requireNonNull(function);
            return new IndexedLongFunction<R>() {
                @Override
                public R apply(int index, long value) {
                    return function.apply(value);
                }
            };
        }

    }
}
