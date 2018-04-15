package com.annimon.stream.function;

import com.annimon.stream.Objects;

/**
 * Represents an operation on input argument.
 *
 * @param <T> the type of the input to the operation
 * @see BiConsumer
 */
public interface Consumer<T> {

    /**
     * Performs operation on argument.
     *
     * @param t  the input argument
     */
    void accept(T t);

    class Util {

        private Util() { }

        /**
         * Composes {@code Consumer} calls.
         *
         * <p>{@code c1.accept(value); c2.accept(value); }
         *
         * @param <T> the type of the input to the operation
         * @param c1  the first {@code Consumer}
         * @param c2  the second {@code Consumer}
         * @return a composed {@code Consumer}
         * @throws NullPointerException if {@code c1} or {@code c2} is null
         */
        public static <T> Consumer<T> andThen(final Consumer<? super T> c1, final Consumer<? super T> c2) {
            return new Consumer<T>() {
                @Override
                public void accept(T value) {
                    c1.accept(value);
                    c2.accept(value);
                }
            };
        }

        /**
         * Creates a safe {@code Consumer}.
         *
         * @param <T> the type of the input to the function
         * @param throwableConsumer  the consumer that may throw an exception
         * @return a {@code Consumer}
         * @throws NullPointerException if {@code throwableConsumer} is null
         * @see #safe(com.annimon.stream.function.ThrowableConsumer, com.annimon.stream.function.Consumer)
         */
        public static <T> Consumer<T> safe(ThrowableConsumer<? super T, Throwable> throwableConsumer) {
            return safe(throwableConsumer, null);
        }

        /**
         * Creates a safe {@code Consumer}.
         *
         * @param <T> the type of the input to the function
         * @param throwableConsumer  the consumer that may throw an exception
         * @param onFailedConsumer  the consumer which applies if exception was thrown
         * @return a {@code Consumer}
         * @throws NullPointerException if {@code throwableConsumer} is null
         * @see #safe(com.annimon.stream.function.ThrowableConsumer)
         */
        public static <T> Consumer<T> safe(
                final ThrowableConsumer<? super T, Throwable> throwableConsumer,
                final Consumer<? super T> onFailedConsumer) {
            return new Consumer<T>() {

                @Override
                public void accept(T value) {
                    Objects.requireNonNull(throwableConsumer);
                    try {
                        throwableConsumer.accept(value);
                    } catch (Throwable ex) {
                        if (onFailedConsumer != null) {
                            onFailedConsumer.accept(value);
                        }
                    }
                }
            };
        }

    }
}
