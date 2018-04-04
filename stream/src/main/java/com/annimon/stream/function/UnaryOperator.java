package com.annimon.stream.function;

/**
 * Operation on a single operand that produces a result of the
 * same type as its operand.
 *
 * @param <T> the type of the operand and result of the operator
 */
public interface UnaryOperator<T> extends Function<T, T> {
    class Util {

        private Util() { }

        /**
         * Returns a unary operator that always returns its input argument.
         *
         * @param <T> the type of the input and output of the operator
         * @return a unary operator that always returns its input argument
         */
        public static <T> UnaryOperator<T> identity() {
            return new UnaryOperator<T>() {
                @Override
                public T apply(T t) {
                    return t;
                }
            };
        }
    }
}
