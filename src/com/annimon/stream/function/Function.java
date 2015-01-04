package com.annimon.stream.function;

/**
 * Operation to convert data from one type to another.
 * 
 * @author aNNiMON
 */
public interface Function {
    
    Object apply(Object value);
    
    public static class Util {
        /**
         * Compose Function calls.
         */
        public static Function andThen(final Function f1, final Function f2) {
            return new Function() {

                public Object apply(Object t) {
                    return f2.apply(f1.apply(t));
                }
            };
        }
    }
}
