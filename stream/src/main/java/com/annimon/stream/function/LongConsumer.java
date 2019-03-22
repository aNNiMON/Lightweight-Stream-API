package com.annimon.stream.function;

import com.annimon.stream.Objects;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Represents an operation on a {@code long}-valued input argument.
 *
 * @since 1.1.4
 * @see Consumer
 */
public interface LongConsumer {

    /**
     * Performs operation on the given argument.
     *
     * @param value  the input argument
     */
    void accept(long value);

    class Util {

        private Util() { }

        /**
         * Composes {@code LongConsumer} calls.
         *
         * <p>{@code c1.accept(value); c2.accept(value); }
         *
         * @param c1  the first {@code LongConsumer}
         * @param c2  the second {@code LongConsumer}
         * @return a composed {@code LongConsumer}
         * @throws NullPointerException if {@code c1} or {@code c2} is null
         */
        public static LongConsumer andThen(
                @NotNull final LongConsumer c1,
                @NotNull final LongConsumer c2) {
            Objects.requireNonNull(c1, "c1");
            Objects.requireNonNull(c2, "c2");
            return new LongConsumer() {
                @Override
                public void accept(long value) {
                    c1.accept(value);
                    c2.accept(value);
                }
            };
        }

        /**
         * Creates a safe {@code LongConsumer}.
         *
         * @param throwableConsumer  the consumer that may throw an exception
         * @return a {@code LongConsumer}
         * @throws NullPointerException if {@code throwableConsumer} is null
         * @since 1.1.7
         * @see #safe(com.annimon.stream.function.ThrowableLongConsumer, com.annimon.stream.function.LongConsumer)
         */
        public static LongConsumer safe(
                @NotNull ThrowableLongConsumer<Throwable> throwableConsumer) {
            return safe(throwableConsumer, null);
        }

        /**
         * Creates a safe {@code LongConsumer}.
         *
         * @param throwableConsumer  the consumer that may throw an exception
         * @param onFailedConsumer  the consumer which applies if exception was thrown
         * @return a {@code LongConsumer}
         * @throws NullPointerException if {@code throwableConsumer} is null
         * @since 1.1.7
         * @see #safe(com.annimon.stream.function.ThrowableLongConsumer)
         */
        public static LongConsumer safe(
                @NotNull final ThrowableLongConsumer<Throwable> throwableConsumer,
                @Nullable final LongConsumer onFailedConsumer) {
            Objects.requireNonNull(throwableConsumer);
            return new LongConsumer() {

                @Override
                public void accept(long value) {
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
