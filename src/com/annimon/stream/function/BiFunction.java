package com.annimon.stream.function;

import com.annimon.stream.Comparator;
import com.annimon.stream.Objects;

/**
 * Operation to convert data by two arguments.
 * 
 * @author aNNiMON
 */
public interface BiFunction {
    
    Object apply(Object value1, Object value2);
    
    public static class Util {
        /**
         * Compose BiFunction calls.
         */
        public static BiFunction andThen(final BiFunction f1, final Function f2) {
            return new BiFunction() {

                public Object apply(Object t, Object u) {
                    return f2.apply(f1.apply(t, u));
                }
            };
        }
        
        public static BiFunction minBy(final Comparator comparator) {
            Objects.requireNonNull(comparator);
            return new BiFunction() {

                public Object apply(Object a, Object b) {
                    return comparator.compare(a, b) <= 0 ? a : b;
                }
            };
        }
        
        public static BiFunction maxBy(final Comparator comparator) {
            Objects.requireNonNull(comparator);
            return new BiFunction() {

                public Object apply(Object a, Object b) {
                    return comparator.compare(a, b) >= 0 ? a : b;
                }
            };
        }
    }
}
