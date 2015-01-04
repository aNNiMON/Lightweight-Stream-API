package com.annimon.stream.function;

import com.annimon.stream.Objects;
import java.util.Comparator;

/**
 * Operation to convert data to type R by two arguments.
 * 
 * @author aNNiMON
 * @param <T> the type of the first argument
 * @param <U> the type of the second argument
 * @param <R> the type of the result of the function
 */
@FunctionalInterface
public interface BiFunction<T, U, R> {
    
    R apply(T value1, U value2);
    
    public static class Util {
        /**
         * Compose BiFunction calls.
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
        
        public static <T> BiFunction<T,T,T> minBy(final Comparator<? super T> comparator) {
            Objects.requireNonNull(comparator);
            return new BiFunction<T, T, T>() {

                @Override
                public T apply(T a, T b) {
                    return comparator.compare(a, b) <= 0 ? a : b;
                }
            };
        }
        
        public static <T> BiFunction<T,T,T> maxBy(final Comparator<? super T> comparator) {
            Objects.requireNonNull(comparator);
            return new BiFunction<T, T, T>() {

                @Override
                public T apply(T a, T b) {
                    return comparator.compare(a, b) >= 0 ? a : b;
                }
            };
        }
    }
}
