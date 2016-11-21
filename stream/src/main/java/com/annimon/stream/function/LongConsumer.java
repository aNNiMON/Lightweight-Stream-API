package com.annimon.stream.function;

/**
 * Represents an operation on a {@code long}-valued input argument.
 *
 * @since 1.1.4
 * @see Consumer
 */
@FunctionalInterface
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
        public static LongConsumer andThen(final LongConsumer c1, final LongConsumer c2) {
            return new LongConsumer() {
                @Override
                public void accept(long value) {
                    c1.accept(value);
                    c2.accept(value);
                }
            };
        }
    }
}
