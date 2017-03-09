package com.annimon.stream.operator;

import com.annimon.stream.function.IntToDoubleFunction;
import com.annimon.stream.iterator.PrimitiveIterator;

public class IntMapToDouble extends PrimitiveIterator.OfDouble {

    private final PrimitiveIterator.OfInt iterator;
    private final IntToDoubleFunction mapper;

    public IntMapToDouble(PrimitiveIterator.OfInt iterator, IntToDoubleFunction mapper) {
        this.iterator = iterator;
        this.mapper = mapper;
    }

    @Override
    public boolean hasNext() {
        return iterator.hasNext();
    }

    @Override
    public double nextDouble() {
        return mapper.applyAsDouble(iterator.nextInt());
    }
}
