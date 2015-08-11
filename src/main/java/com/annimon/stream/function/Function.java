package com.annimon.stream.function;

/**
 * Operation to convert data with type T to type R.
 * 
 * @author aNNiMON
 * @param <T> the type of the input to the function
 * @param <R> the type of the result of the function
 */
@FunctionalInterface
public interface Function<T, R> {
    
    R apply(T value);
    
    public static class Util {
        /**
         * Compose Function calls.
         */
        public static <T, R, V> Function<T, V> andThen(
                final Function<? super T, ? extends R> f1,
                final Function<? super R, ? extends V> f2) {
            return new Function<T, V>() {

                @Override
                public V apply(T t) {
                    return f2.apply(f1.apply(t));
                }
            };
        }
    }
}
