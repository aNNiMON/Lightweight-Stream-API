package com.annimon.stream.function;

import com.annimon.stream.Objects;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Represents an operation on index and input argument.
 *
 * @since 1.2.1
 */
public interface IndexedLongConsumer {

    /**
     * Performs operation on argument.
     *
     * @param index  the index
     * @param value  the input argument
     */
    void accept(int index, long value);

    class Util {

        private Util() { }

        /**
         * Composes {@code IndexedLongConsumer} calls.
         *
         * <p>{@code c1.accept(index, value); c2.accept(index, value); }
         *
         * @param c1  the first {@code IndexedLongConsumer}
         * @param c2  the second {@code IndexedLongConsumer}
         * @return a composed {@code IndexedLongConsumer}
         * @throws NullPointerException if {@code c1} or {@code c2} is null
         */
        public static IndexedLongConsumer andThen(
                @NotNull final IndexedLongConsumer c1,
                @NotNull final IndexedLongConsumer c2) {
            Objects.requireNonNull(c1, "c1");
            Objects.requireNonNull(c2, "c2");
            return new IndexedLongConsumer() {
                @Override
                public void accept(int index, long value) {
                    c1.accept(index, value);
                    c2.accept(index, value);
                }
            };
        }

        /**
         * Returns an {@code IndexedLongConsumer} that accepts {@code IntConsumer}
         * for index and {@code LongConsumer} for value.
         *
         * <pre><code>
         *  if (c1 != null)
         *      c1.accept(index);
         *  if (c2 != null)
         *      c2.accept(object);
         * </code></pre>
         *
         * @param c1  the {@code IntConsumer} for index, can be null
         * @param c2  the {@code LongConsumer} for value, can be null
         * @return an {@code IndexedLongConsumer}
         */
        public static IndexedLongConsumer accept(
                @Nullable final IntConsumer c1,
                @Nullable final LongConsumer c2) {
            return new IndexedLongConsumer() {
                @Override
                public void accept(int index, long value) {
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
