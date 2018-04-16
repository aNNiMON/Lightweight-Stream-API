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
         * Wraps a {@link IntConsumer} and returns {@code IndexedIntConsumer}.
         *
         * @param consumer  the consumer to wrap
         * @return a wrapped {@code IndexedIntConsumer}
         * @throws NullPointerException if {@code consumer} is null
         */
        public static IndexedIntConsumer wrap(
                final IntConsumer consumer) {
            Objects.requireNonNull(consumer);
            return new IndexedIntConsumer() {
                @Override
                public void accept(int index, int value) {
                    consumer.accept(value);
                }
            };
        }

        /**
         * Returns an {@code IndexedIntConsumer} that accepts {@code IntConsumer}
         * for index and {@code IntConsumer} for object.
         *
         * <pre><code>
         *  if (c1 != null)
         *      c1.accept(index);
         *  if (c2 != null)
         *      c2.accept(object);
         * </code></pre>
         *
         * @param c1  the {@code IntConsumer} for index, can be null
         * @param c2  the {@code IntConsumer} for object, can be null
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
