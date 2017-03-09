package com.annimon.stream.operator;

import com.annimon.stream.function.DoubleToIntFunction;
import com.annimon.stream.iterator.PrimitiveIterator;

public class DoubleMapToInt extends PrimitiveIterator.OfInt {

    private final PrimitiveIterator.OfDouble iterator;
    private final DoubleToIntFunction mapper;

    public DoubleMapToInt(PrimitiveIterator.OfDouble iterator, DoubleToIntFunction mapper) {
        this.iterator = iterator;
        this.mapper = mapper;
    }

    @Override
    public boolean hasNext() {
        return iterator.hasNext();
    }

    @Override
    public int nextInt() {
        return mapper.applyAsInt(iterator.nextDouble());
    }
}
