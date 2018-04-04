package com.annimon.stream;

import com.annimon.stream.function.BiConsumer;
import com.annimon.stream.function.Function;
import com.annimon.stream.function.Supplier;

/**
 * The Collector of stream data.
 *
 * @param <T> the type of input elements to the reduction operation
 * @param <A> the mutable accumulation type of the reduction operation
 * @param <R> the result type of the reduction operation
 * @see Stream#collect(com.annimon.stream.Collector)
 */
public interface Collector<T, A, R> {

    /**
     * Function provides new containers.
     *
     * @return {@code Supplier}
     */
    Supplier<A> supplier();

    /**
     * Function folds elements into container.
     *
     * @return {@code BiConsumer}
     */
    BiConsumer<A, T> accumulator();

    /**
     * Function produces result by transforming intermediate type.
     *
     * @return {@code Function}
     */
    Function<A, R> finisher();
}