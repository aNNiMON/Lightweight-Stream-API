package com.annimon.stream.function;

/**
 * Operation with input arguments without return result.
 * 
 * @author aNNiMON
 * @param <T> the type of the input to the operation
 */
@FunctionalInterface
public interface Consumer<T> {
    
    void accept(T value);
    
    public static class Util {
        /**
         * Compose Consumer calls.
         */
        public <T> Consumer<T> andThen(final Consumer<? super T> c1, final Consumer<? super T> c2) {
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
