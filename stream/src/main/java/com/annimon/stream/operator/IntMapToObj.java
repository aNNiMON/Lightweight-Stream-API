package com.annimon.stream.operator;

import com.annimon.stream.function.IntFunction;
import com.annimon.stream.iterator.LsaIterator;
import com.annimon.stream.iterator.PrimitiveIterator;
import org.jetbrains.annotations.NotNull;

public class IntMapToObj<R> extends LsaIterator<R> {

    private final PrimitiveIterator.OfInt iterator;
    private final IntFunction<? extends R> mapper;

    public IntMapToObj(
            @NotNull PrimitiveIterator.OfInt iterator, @NotNull IntFunction<? extends R> mapper) {
        this.iterator = iterator;
        this.mapper = mapper;
    }

    @Override
    public boolean hasNext() {
        return iterator.hasNext();
    }

    @Override
    public R nextIteration() {
        return mapper.apply(iterator.nextInt());
    }
}
