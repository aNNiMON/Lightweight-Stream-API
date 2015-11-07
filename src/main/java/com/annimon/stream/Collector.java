package com.annimon.stream;

import com.annimon.stream.function.BiConsumer;
import com.annimon.stream.function.Function;
import com.annimon.stream.function.Supplier;

/**
 * Collector of stream data.
 * 
 * @param <T> the type of input elements to the reduction operation
 * @param <A> the mutable accumulation type of the reduction operation
 * @param <R> the result type of the reduction operation
 */
public interface Collector<T, A, R> {
    
    /**
     * Function for provide new containers.
     * 
     * @return {@code Supplier}
     */
    Supplier<A> supplier();
    
    /**
     * Function that folds elements into container.
     * 
     * @return {@code Supplier}
     */
    BiConsumer<A, T> accumulator();
    
    /**
     * Function that produce result by transforming intermediate type.
     * 
     * @return {@code Function}
     */
    Function<A, R> finisher();
}