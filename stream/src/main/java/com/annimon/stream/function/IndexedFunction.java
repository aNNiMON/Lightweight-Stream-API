package com.annimon.stream.function;

import com.annimon.stream.Objects;

/**
 * Represents a function which produces result from index and input argument.
 *
 * @param <T> the type of the input of the function
 * @param <R> the type of the result of the function
 * @since 1.1.6
 */
public interface IndexedFunction<T, R> {

    /**
     * Applies this function to the given argument.
     *
     * @param index  the index
     * @param t  an argument
     * @return the function result
     */
    R apply(int index, T t);

    class Util {

        private Util() { }

        /**
         * Wraps {@link Function} and returns {@code IndexedFunction}.
         *
         * @param <T> the type of the input argument
         * @param <R> the type of the result
         * @param function  the function to wrap
         * @return a wrapped {@code IndexedFunction}
         * @throws NullPointerException if {@code function} is null
         */
        public static <T, R> IndexedFunction<T, R> wrap(
                final Function<? super T, ? extends R> function) {
            Objects.requireNonNull(function);
            return new IndexedFunction<T, R>() {

                @Override
                public R apply(int index, T t) {
                    return function.apply(t);
                }
            };
        }

    }
}
