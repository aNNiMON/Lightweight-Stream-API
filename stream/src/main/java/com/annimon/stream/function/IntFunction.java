package com.annimon.stream.function;

/**
 * Represents a function that accepts an int-valued argument and produces a
 * result.  This is the {@code int}-consuming primitive specialization for
 * {@link Function}.
 *
 * <p>This is a <a href="package-summary.html">functional interface</a>
 * whose functional method is {@link #apply(int)}.
 *
 * @param <R> the type of the result of the function
 *
 * @see Function
 */
public interface IntFunction<R> {

    /**
     * Applies this function to the given argument.
     *
     * @param value the function argument
     * @return the function result
     */
    R apply(int value);

    class Util {

        private Util() { }

        /**
         * Creates a safe {@code IntFunction},
         *
         * @param <R> the type of the result of the function
         * @param throwableFunction  the function that may throw an exception
         * @return the function result or {@code null} if exception was thrown
         * @throws NullPointerException if {@code throwableFunction} is null
         * @since 1.1.7
         * @see #safe(com.annimon.stream.function.ThrowableIntFunction, java.lang.Object)
         */
        public static <R> IntFunction<R> safe(
                ThrowableIntFunction<? extends R, Throwable> throwableFunction) {
            return Util.<R>safe(throwableFunction, null);
        }

        /**
         * Creates a safe {@code IntFunction},
         *
         * @param <R> the type of the result of the function
         * @param throwableFunction  the function that may throw an exception
         * @param resultIfFailed  the result which returned if exception was thrown
         * @return the function result or {@code resultIfFailed}
         * @throws NullPointerException if {@code throwableFunction} is null
         * @since 1.1.7
         */
        public static <R> IntFunction<R> safe(
                final ThrowableIntFunction<? extends R, Throwable> throwableFunction,
                final R resultIfFailed) {
            return new IntFunction<R>() {

                @Override
                public R apply(int value) {
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