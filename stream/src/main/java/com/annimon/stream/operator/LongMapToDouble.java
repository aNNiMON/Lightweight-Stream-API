package com.annimon.stream.operator;

import com.annimon.stream.PrimitiveIterator;
import com.annimon.stream.function.LongToDoubleFunction;

public class LongMapToDouble extends PrimitiveIterator.OfDouble {

    private final PrimitiveIterator.OfLong iterator;
    private final LongToDoubleFunction mapper;

    public LongMapToDouble(PrimitiveIterator.OfLong iterator, LongToDoubleFunction mapper) {
        this.iterator = iterator;
        this.mapper = mapper;
    }

    @Override
    public boolean hasNext() {
        return iterator.hasNext();
    }

    @Override
    public double nextDouble() {
        return mapper.applyAsDouble(iterator.nextLong());
    }
}
