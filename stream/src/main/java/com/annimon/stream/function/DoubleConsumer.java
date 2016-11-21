package com.annimon.stream.function;

/**
 * Represents an operation on a {@code double}-valued input argument.
 *
 * @since 1.1.4
 * @see Consumer
 */
@FunctionalInterface
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
        public static DoubleConsumer andThen(final DoubleConsumer c1, final DoubleConsumer c2) {
            return new DoubleConsumer() {
                @Override
                public void accept(double value) {
                    c1.accept(value);
                    c2.accept(value);
                }
            };
        }
    }
}
