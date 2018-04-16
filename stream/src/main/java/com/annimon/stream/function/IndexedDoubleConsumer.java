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
         * Wraps a {@link DoubleConsumer} and returns {@code IndexedDoubleConsumer}.
         *
         * @param consumer  the consumer to wrap
         * @return a wrapped {@code IndexedDoubleConsumer}
         * @throws NullPointerException if {@code consumer} is null
         */
        public static IndexedDoubleConsumer wrap(
                final DoubleConsumer consumer) {
            Objects.requireNonNull(consumer);
            return new IndexedDoubleConsumer() {
                @Override
                public void accept(int index, double value) {
                    consumer.accept(value);
                }
            };
        }

        /**
         * Returns an {@code IndexedDoubleConsumer} that accepts {@code IntConsumer}
         * for index and {@code DoubleConsumer} for object.
         *
         * <pre><code>
         *  if (c1 != null)
         *      c1.accept(index);
         *  if (c2 != null)
         *      c2.accept(object);
         * </code></pre>
         *
         * @param c1  the {@code IntConsumer} for index, can be null
         * @param c2  the {@code DoubleConsumer} for object, can be null
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
