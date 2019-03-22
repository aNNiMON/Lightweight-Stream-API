package com.annimon.stream.function;

import com.annimon.stream.Objects;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Represents an operation on a {@code double}-valued input argument.
 *
 * @since 1.1.4
 * @see Consumer
 */
public interface DoubleConsumer {

    /**
     * Performs operation on the given argument.
     *
     * @param value  the input argument
     */
    void accept(double value);

    class Util {

        private Util() { }

        /**
         * Composes {@code DoubleConsumer} calls.
         *
         * <p>{@code c1.accept(value); c2.accept(value); }
         *
         * @param c1  the first {@code DoubleConsumer}
         * @param c2  the second {@code DoubleConsumer}
         * @return a composed {@code DoubleConsumer}
         * @throws NullPointerException if {@code c1} or {@code c2} is null
         */
        public static DoubleConsumer andThen(
                @NotNull final DoubleConsumer c1,
                @NotNull final DoubleConsumer c2) {
            Objects.requireNonNull(c1, "c1");
            Objects.requireNonNull(c2, "c2");
            return new DoubleConsumer() {
                @Override
                public void accept(double value) {
                    c1.accept(value);
                    c2.accept(value);
                }
            };
        }

        /**
         * Creates a safe {@code DoubleConsumer}.
         *
         * @param throwableConsumer  the consumer that may throw an exception
         * @return a {@code DoubleConsumer}
         * @throws NullPointerException if {@code throwableConsumer} is null
         * @since 1.1.7
         * @see #safe(com.annimon.stream.function.ThrowableDoubleConsumer, com.annimon.stream.function.DoubleConsumer)
         */
        public static DoubleConsumer safe(
                @NotNull ThrowableDoubleConsumer<Throwable> throwableConsumer) {
            return safe(throwableConsumer, null);
        }

        /**
         * Creates a safe {@code DoubleConsumer}.
         *
         * @param throwableConsumer  the consumer that may throw an exception
         * @param onFailedConsumer  the consumer which applies if exception was thrown
         * @return a {@code DoubleConsumer}
         * @throws NullPointerException if {@code throwableConsumer} is null
         * @since 1.1.7
         * @see #safe(com.annimon.stream.function.ThrowableDoubleConsumer)
         */
        public static DoubleConsumer safe(
                @NotNull final ThrowableDoubleConsumer<Throwable> throwableConsumer,
                @Nullable final DoubleConsumer onFailedConsumer) {
            Objects.requireNonNull(throwableConsumer);
            return new DoubleConsumer() {

                @Override
                public void accept(double value) {
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
