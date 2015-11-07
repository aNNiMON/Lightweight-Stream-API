package com.annimon.stream.function;

/**
 * Represents an operation on input argument.
 * 
 * @param <T> the type of the input to the operation
 * @see BiConsumer
 */
@FunctionalInterface
public interface Consumer<T> {
    
    /**
     * Performs operation on argument.
     * 
     * @param value  the input argument
     */
    void accept(T value);
    
    class Util {
        
        private Util() { }
        
        /**
         * Compose {@code Consumer} calls.
         * 
         * <p>{@code c1.accept(value); c2.accept(value); }
         * 
         * @param <T> the type of the input to the operation
         * @param c1  first {@code Consumer}
         * @param c2  second {@code Consumer}
         * @return a composed {@code Consumer}
         * @throws NullPointerException if {@code c1} or {@code c2} is null
         */
        public static <T> Consumer<T> andThen(final Consumer<? super T> c1, final Consumer<? super T> c2) {
            return new Consumer<T>() {
                @Override
                public void accept(T value) {
                    c1.accept(value);
                    c2.accept(value);
                }
            };
        }
    }
}
