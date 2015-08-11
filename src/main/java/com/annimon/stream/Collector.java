package com.annimon.stream;

import com.annimon.stream.function.BiConsumer;
import com.annimon.stream.function.Function;
import com.annimon.stream.function.Supplier;

/**
 * Collector of stream data.
 * 
 * @author aNNiMON
 * @param <T> the type of input elements to the reduction operation
 * @param <A> the mutable accumulation type of the reduction operation
 * @param <R> the result type of the reduction operation
 */
public interface Collector<T, A, R> {
    
    Supplier<A> supplier();
    
    BiConsumer<A, T> accumulator();
    
    Function<A, R> finisher();
}