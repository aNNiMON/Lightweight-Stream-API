package com.annimon.stream.operator;

import com.annimon.stream.function.DoubleFunction;
import com.annimon.stream.iterator.LsaIterator;
import com.annimon.stream.iterator.PrimitiveIterator;

public class DoubleMapToObj<R> extends LsaIterator<R> {

    private final PrimitiveIterator.OfDouble iterator;
    private final DoubleFunction<? extends R> mapper;

    public DoubleMapToObj(PrimitiveIterator.OfDouble iterator, DoubleFunction<? extends R> mapper) {
        this.iterator = iterator;
        this.mapper = mapper;
    }

    @Override
    public boolean hasNext() {
        return iterator.hasNext();
    }

    @Override
    public R nextIteration() {
        return mapper.apply(iterator.nextDouble());
    }
}
