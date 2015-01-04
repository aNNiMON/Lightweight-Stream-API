package com.annimon.stream.function;

/**
 * Operation with two input arguments without return result.
 * 
 * @author aNNiMON
 * @param <T> the type of the first argument
 * @param <U> the type of the second argument
 */
@FunctionalInterface
public interface BiConsumer<T, U> {
    
    void accept(T t, U u);
    
    public static class Util {
        /**
         * Compose Consumer calls.
         */
        public <T, U> BiConsumer<T, U> andThen(
                final BiConsumer<? super T, ? super U> c1,
                final BiConsumer<? super T, ? super U> c2) {
            return new BiConsumer<T, U>() {
                @Override
                public void accept(T t, U u) {
                    c1.accept(t, u);
                    c2.accept(t, u);
                }
            };
        }
    }
}
