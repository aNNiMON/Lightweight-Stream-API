package com.annimon.stream.function;

import com.annimon.stream.Objects;

/**
 * Represents an operation on index and input argument.
 *
 * @since 1.2.1
 */
public interface IndexedDoubleConsumer {

    /**
     * Performs operation on argument.
     *
     * @param index  the index
     * @param value  the input argument
     */
    void accept(int index, double value);

    class Util {

        private Util() { }

        /**
         * Composes {@code IndexedDoubleConsumer} calls.
         *
         * <p>{@code c1.accept(index, value); c2.accept(index, value); }
         *
         * @param c1  the first {@code IndexedDoubleConsumer}
         * @param c2  the second {@code IndexedDoubleConsumer}
         * @return a composed {@code IndexedDoubleConsumer}
         * @throws NullPointerException if {@code c1} or {@code c2} is null
         */
        public static IndexedDoubleConsumer andThen(final IndexedDoubleConsumer c1, final IndexedDoubleConsumer c2) {
            return new IndexedDoubleConsumer() {
                @Override
                public void accept(int index, double value) {
                    c1.accept(index, value);
                    c2.accept(index, value);
                }
            };
        }

        /**
         * Returns an {@code IndexedDoubleConsumer} that accepts {@code IntConsumer}
         * for index and {@code DoubleConsumer} for value.
         *
         * <pre><code>
         *  if (c1 != null)
         *      c1.accept(index);
         *  if (c2 != null)
         *      c2.accept(object);
         * </code></pre>
         *
         * @param c1  the {@code IntConsumer} for index, can be null
         * @param c2  the {@code DoubleConsumer} for value, can be null
         * @return an {@code IndexedDoubleConsumer}
         */
        public static IndexedDoubleConsumer accept(
                final IntConsumer c1, final DoubleConsumer c2) {
            return new IndexedDoubleConsumer() {
                @Override
                public void accept(int index, double value) {
                    if (c1 != null) {
                        c1.accept(index);
                    }
                    if (c2 != null) {
                        c2.accept(value);
                    }
                }
            };
        }

    }
}
