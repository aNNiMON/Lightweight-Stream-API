package com.annimon.stream.function;

/**
 * Represents a function which produces result from two input arguments.
 * 
 * @param <T> the type of the first argument
 * @param <U> the type of the second argument
 * @param <R> the type of the result of the function
 */
@FunctionalInterface
public interface BiFunction<T, U, R> {
    
    /**
     * Applies this function to the given arguments.
     * 
     * @param value1  the first argument
     * @param value2  the second argument
     * @return the function result
     */
    R apply(T value1, U value2);
    
    class Util {
        
        private Util() { }
        
        /**
         * Compose {@code BiFunction} calls.
         * 
         * <p>{@code f2.apply(f1.apply(t, u)) }
         * 
         * @param <T> the type of the first argument
         * @param <U> the type of the second argument
         * @param <R> the type of the result of the {@code BiFunction f1}
         * @param <V> the type of the result of composed function {@code f2}
         * @param f1  the {@code BiFunction} which is called first
         * @param f2  the function for transform {@code BiFunction f1} result to the type {@code V}
         * @return the result of composed function
         * @throws NullPointerException if {@code f1} or {@code f2} or result of {@code BiFunction f1} is null
         */
        public static <T, U, R, V> BiFunction<T, U, V> andThen(
                final BiFunction<? super T, ? super U, ? extends R> f1,
                final Function<? super R, ? extends V> f2) {
            return new BiFunction<T, U, V>() {

                @Override
                public V apply(T t, U u) {
                    return f2.apply(f1.apply(t, u));
                }
            };
        }
    }
}
