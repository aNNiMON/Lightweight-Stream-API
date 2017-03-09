package com.annimon.stream.operator;

import com.annimon.stream.function.DoubleToLongFunction;
import com.annimon.stream.iterator.PrimitiveIterator;

public class DoubleMapToLong extends PrimitiveIterator.OfLong {

    private final PrimitiveIterator.OfDouble iterator;
    private final DoubleToLongFunction mapper;

    public DoubleMapToLong(PrimitiveIterator.OfDouble iterator, DoubleToLongFunction mapper) {
        this.iterator = iterator;
        this.mapper = mapper;
    }

    @Override
    public boolean hasNext() {
        return iterator.hasNext();
    }

    @Override
    public long nextLong() {
        return mapper.applyAsLong(iterator.nextDouble());
    }
}
