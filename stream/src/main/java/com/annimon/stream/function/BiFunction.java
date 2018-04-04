package com.annimon.stream.function;

import com.annimon.stream.Objects;

/**
 * Represents a function which produces result from two input arguments.
 *
 * @param <T> the type of the first argument
 * @param <U> the type of the second argument
 * @param <R> the type of the result of the function
 */
public interface BiFunction<T, U, R> {

    /**
     * Applies this function to the given arguments.
     *
     * @param value1  the first argument
     * @param value2  the second argument
     * @return the function result
     */
    R apply(T value1, U value2);

    class Util {

        private Util() { }

        /**
         * Composes {@code BiFunction} calls.
         *
         * <p>{@code f2.apply(f1.apply(t, u)) }
         *
         * @param <T> the type of the first argument
         * @param <U> the type of the second argument
         * @param <R> the type of the result of the {@code BiFunction f1}
         * @param <V> the type of the result of composed function {@code f2}
         * @param f1  the {@code BiFunction} which is called first
         * @param f2  the function for transform {@code BiFunction f1} result to the type {@code V}
         * @return the result of composed function
         * @throws NullPointerException if {@code f1} or {@code f2} or result of {@code BiFunction f1} is null
         */
        public static <T, U, R, V> BiFunction<T, U, V> andThen(
                final BiFunction<? super T, ? super U, ? extends R> f1,
                final Function<? super R, ? extends V> f2) {
            return new BiFunction<T, U, V>() {

                @Override
                public V apply(T t, U u) {
                    return f2.apply(f1.apply(t, u));
                }
            };
        }

        /**
         * Returns a {@code BiFunction} that reverses the input arguments order
         * of the specified {@code BiFunction}.
         *
         * @param <T> the type of the first argument of the given function
         *            and the second argument of the reversed function
         * @param <U> the type of the second argument of the given function
         *            and the first argument of the reversed function
         * @param <R> the type of the result of the given function
         *            and of the reversed function
         * @param function  the {@code BiFunction} to reverse arguments
         * @return the reversed function
         * @throws NullPointerException if {@code function} is null
         * @since 1.1.6
         */
        public static <T, U, R> BiFunction<U, T, R> reverse(
                final BiFunction<? super T, ? super U, ? extends R> function) {
            Objects.requireNonNull(function);
            return new BiFunction<U, T, R>() {
                @Override
                public R apply(U u, T t) {
                    return function.apply(t, u);
                }
            };
        }
    }
}
