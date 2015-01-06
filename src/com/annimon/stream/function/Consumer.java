package com.annimon.stream.function;

/**
 * Operation with input arguments without return result.
 * 
 * @author aNNiMON
 */
public interface Consumer {
    
    void accept(Object value);
    
    public static class Util {
        /**
         * Compose Consumer calls.
         */
        public static Consumer andThen(final Consumer c1, final Consumer c2) {
            return new Consumer() {
                public void accept(Object value) {
                    c1.accept(value);
                    c2.accept(value);
                }
            };
        }
    }
}
