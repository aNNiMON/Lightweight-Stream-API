package com.annimon.stream.function;

import com.annimon.stream.Objects;

/**
 * Represents a function which produces result from index and two input arguments.
 *
 * @param <T> the type of the first argument
 * @param <U> the type of the second argument
 * @param <R> the type of the result of the function
 * @since 1.1.6
 */
public interface IndexedBiFunction<T, U, R> {

    /**
     * Applies this function to the given arguments.
     *
     * @param index  the index
     * @param value1  the first argument
     * @param value2  the second argument
     * @return the function result
     */
    R apply(int index, T value1, U value2);

    class Util {

        private Util() { }

        /**
         * Wraps {@link BiFunction} and returns {@code IndexedBiFunction}.
         *
         * @param <T> the type of the first argument
         * @param <U> the type of the second argument
         * @param <R> the type of the result
         * @param function  the {@code BiFunction} to wrap
         * @return a wrapped {@code IndexedBiFunction}
         * @throws NullPointerException if {@code function} is null
         */
        public static <T, U, R> IndexedBiFunction<T, U, R> wrap(
                final BiFunction<? super T, ? super U, ? extends R> function) {
            Objects.requireNonNull(function);
            return new IndexedBiFunction<T, U, R>() {

                @Override
                public R apply(int index, T t, U u) {
                    return function.apply(t, u);
                }
            };
        }
    }
}
