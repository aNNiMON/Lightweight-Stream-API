package com.annimon.stream.operator;

import com.annimon.stream.LsaIterator;
import com.annimon.stream.PrimitiveIterator;
import com.annimon.stream.function.IntFunction;

public class IntMapToObj<R> extends LsaIterator<R> {

    private final PrimitiveIterator.OfInt iterator;
    private final IntFunction<? extends R> mapper;

    public IntMapToObj(PrimitiveIterator.OfInt iterator, IntFunction<? extends R> mapper) {
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
