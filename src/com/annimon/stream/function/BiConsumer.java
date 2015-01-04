package com.annimon.stream.function;

/**
 * Operation with two input arguments without return result.
 * 
 * @author aNNiMON
 */
public interface BiConsumer {
    
    void accept(Object t, Object u);
    
    public static class Util {
        /**
         * Compose Consumer calls.
         */
        public BiConsumer andThen(final BiConsumer c1, final BiConsumer c2) {
            return new BiConsumer() {
                public void accept(Object t, Object u) {
                    c1.accept(t, u);
                    c2.accept(t, u);
                }
            };
        }
    }
}
