package com.annimon.stream.function;

import com.annimon.stream.Objects;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Represents a function which supply a result.
 *
 * @param <T> the type of the result
 */
public interface Supplier<T> {

    /**
     * Gets a result.
     *
     * @return a result
     */
    T get();

    class Util {

        private Util() { }

        /**
         * Creates a safe {@code Supplier}.
         *
         * @param <T> the type of the result
         * @param throwableSupplier  the supplier that may throw an exception
         * @return a {@code Supplier}
         * @throws NullPointerException if {@code throwableSupplier} is null
         * @since 1.1.7
         * @see #safe(com.annimon.stream.function.ThrowableSupplier, java.lang.Object)
         */
        public static <T> Supplier<T> safe(
                @NotNull ThrowableSupplier<? extends T, Throwable> throwableSupplier) {
            return Util.<T>safe(throwableSupplier, null);
        }

        /**
         * Creates a safe {@code Supplier}.
         *
         * @param <T> the type of the result
         * @param throwableSupplier  the supplier that may throw an exception
         * @param resultIfFailed  the result which returned if exception was thrown
         * @return a {@code Supplier}
         * @throws NullPointerException if {@code throwableSupplier} is null
         * @since 1.1.7
         */
        public static <T> Supplier<T> safe(
                @NotNull final ThrowableSupplier<? extends T, Throwable> throwableSupplier,
                @Nullable final T resultIfFailed) {
            Objects.requireNonNull(throwableSupplier);
            return new Supplier<T>() {

                @Override
                public T get() {
                    try {
                        return throwableSupplier.get();
                    } catch (Throwable ex) {
                        return resultIfFailed;
                    }
                }
            };
        }

    }
}
