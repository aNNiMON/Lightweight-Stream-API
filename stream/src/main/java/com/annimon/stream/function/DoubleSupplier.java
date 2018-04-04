package com.annimon.stream.function;

/**
 * Represents a supplier of {@code double}-valued results.
 *
 * @since 1.1.4
 * @see Supplier
 */
public interface DoubleSupplier {

    /**
     * Gets a result.
     *
     * @return a result
     */
    double getAsDouble();

    class Util {

        private Util() { }

        /**
         * Creates a safe {@code DoubleSupplier}.
         *
         * @param throwableSupplier  the supplier that may throw an exception
         * @return a {@code DoubleSupplier}
         * @throws NullPointerException if {@code throwableSupplier} is null
         * @since 1.1.7
         * @see #safe(com.annimon.stream.function.ThrowableDoubleSupplier, double)
         */
        public static DoubleSupplier safe(ThrowableDoubleSupplier<Throwable> throwableSupplier) {
            return safe(throwableSupplier, 0.0);
        }

        /**
         * Creates a safe {@code DoubleSupplier}.
         *
         * @param throwableSupplier  the supplier that may throw an exception
         * @param resultIfFailed  the result which returned if exception was thrown
         * @return a {@code DoubleSupplier}
         * @throws NullPointerException if {@code throwableSupplier} is null
         * @since 1.1.7
         */
        public static DoubleSupplier safe(
                final ThrowableDoubleSupplier<Throwable> throwableSupplier,
                final double resultIfFailed) {
            return new DoubleSupplier() {

                @Override
                public double getAsDouble() {
                    try {
                        return throwableSupplier.getAsDouble();
                    } catch (Throwable ex) {
                        return resultIfFailed;
                    }
                }
            };
        }

    }
}
