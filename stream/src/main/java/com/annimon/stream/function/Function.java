package com.annimon.stream.function;

/**
 * Represents a function which produces result from input arguments.
 *
 * @param <T> the type of the input of the function
 * @param <R> the type of the result of the function
 */
public interface Function<T, R> {

    /**
     * Applies this function to the given argument.
     *
     * @param t  an argument
     * @return the function result
     */
    R apply(T t);

    class Util {

        private Util() { }

        /**
         * Composes {@code Function} calls.
         *
         * <p>{@code f1.apply(f2.apply(v)) }
         *
         * @param <V> the type of the input argument of first function
         * @param <T> the type of the result of {@code f2} and input argument of {@code f1}
         * @param <R> the type of the result of composed function {@code f1}
         * @param f1  the function for transform {@code Function f2} result to the type {@code R}
         * @param f2  the {@code Function} which is called first
         * @return the result of composed function
         * @throws NullPointerException if {@code f1} or {@code f2} or result of {@code Function f1} is null
         * @see #andThen(com.annimon.stream.function.Function, com.annimon.stream.function.Function)
         */
        public static <V, T, R> Function<V, R> compose(
                final Function<? super T, ? extends R> f1,
                final Function<? super V, ? extends T> f2) {
            return Util.<V, T, R>andThen(f2, f1);
        }

        /**
         * Composes {@code Function} calls.
         *
         * <p>{@code f2.apply(f1.apply(t)) }
         *
         * @param <T> the type of the input argument of first function
         * @param <R> the type of the result of {@code f1} and input argument of {@code f2}
         * @param <V> the type of the result of composed function {@code f2}
         * @param f1  the {@code Function} which is called first
         * @param f2  the function for transform {@code Function f1} result to the type {@code V}
         * @return the result of composed function
         * @throws NullPointerException if {@code f1} or {@code f2} or result of {@code Function f1} is null
         * @see #compose(com.annimon.stream.function.Function, com.annimon.stream.function.Function)
         */
        public static <T, R, V> Function<T, V> andThen(
                final Function<? super T, ? extends R> f1,
                final Function<? super R, ? extends V> f2) {
            return new Function<T, V>() {

                @Override
                public V apply(T t) {
                    return f2.apply(f1.apply(t));
                }
            };
        }

        /**
         * Creates a safe {@code Function},
         *
         * @param <T> the type of the input of the function
         * @param <R> the type of the result of the function
         * @param throwableFunction  the function that may throw an exception
         * @return the function result or {@code null} if exception was thrown
         * @throws NullPointerException if {@code throwableFunction} is null
         * @see #safe(com.annimon.stream.function.ThrowableFunction, java.lang.Object)
         */
        public static <T, R> Function<T, R> safe(
                ThrowableFunction<? super T, ? extends R, Throwable> throwableFunction) {
            return Util.<T, R>safe(throwableFunction, null);
        }

        /**
         * Creates a safe {@code Function},
         *
         * @param <T> the type of the input of the function
         * @param <R> the type of the result of the function
         * @param throwableFunction  the function that may throw an exception
         * @param resultIfFailed  the result which returned if exception was thrown
         * @return the function result or {@code resultIfFailed}
         * @throws NullPointerException if {@code throwableFunction} is null
         * @see #safe(com.annimon.stream.function.ThrowableFunction)
         */
        public static <T, R> Function<T, R> safe(
                final ThrowableFunction<? super T, ? extends R, Throwable> throwableFunction,
                final R resultIfFailed) {
            return new Function<T, R>() {

                @Override
                public R apply(T value) {
                    try {
                        return throwableFunction.apply(value);
                    } catch (Throwable throwable) {
                        return resultIfFailed;
                    }
                }
            };
        }

    }
}
