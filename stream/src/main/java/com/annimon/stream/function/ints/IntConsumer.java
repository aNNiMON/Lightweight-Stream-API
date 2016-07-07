package com.annimon.stream.function.ints;

import com.annimon.stream.function.FunctionalInterface;

/**
 * Created by andrew on 03.07.16.
 */
@FunctionalInterface
public interface IntConsumer {

    /**
     * Performs this operation on the given argument.
     *
     * @param value the input argument
     */
    void accept(int value);
}
