package com.annimon.stream.function;

import com.annimon.stream.Objects;

/**
 * Represents an operation on index and input argument.
 *
 * @since 1.2.1
 */
public interface IndexedIntConsumer {

    /**
     * Performs operation on argument.
     *
     * @param index  the index
     * @param value  the input argument
     */
    void accept(int index, int value);

    class Util {

        private Util() { }

        /**
         * Composes {@code IndexedIntConsumer} calls.
         *
         * <p>{@code c1.accept(index, value); c2.accept(index, value); }
         *
         * @param c1  the first {@code IndexedIntConsumer}
         * @param c2  the second {@code IndexedIntConsumer}
         * @return a composed {@code IndexedIntConsumer}
         * @throws NullPointerException if {@code c1} or {@code c2} is null
         */
        public static IndexedIntConsumer andThen(final IndexedIntConsumer c1, final IndexedIntConsumer c2) {
            return new IndexedIntConsumer() {
                @Override
                public void accept(int index, int value) {
                    c1.accept(index, value);
                    c2.accept(index, value);
                }
            };
        }

        /**
         * Returns an {@code IndexedIntConsumer} that accepts {@code IntConsumer}
         * for index and {@code IntConsumer} for value.
         *
         * <pre><code>
         *  if (c1 != null)
         *      c1.accept(index);
         *  if (c2 != null)
         *      c2.accept(object);
         * </code></pre>
         *
         * @param c1  the {@code IntConsumer} for index, can be null
         * @param c2  the {@code IntConsumer} for value, can be null
         * @return an {@code IndexedIntConsumer}
         */
        public static IndexedIntConsumer accept(
                final IntConsumer c1, final IntConsumer c2) {
            return new IndexedIntConsumer() {
                @Override
                public void accept(int index, int value) {
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
