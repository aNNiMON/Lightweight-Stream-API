package com.annimon.stream.operator;

import com.annimon.stream.function.IntToLongFunction;
import com.annimon.stream.iterator.PrimitiveIterator;

public class IntMapToLong extends PrimitiveIterator.OfLong {

    private final PrimitiveIterator.OfInt iterator;
    private final IntToLongFunction mapper;

    public IntMapToLong(PrimitiveIterator.OfInt iterator, IntToLongFunction mapper) {
        this.iterator = iterator;
        this.mapper = mapper;
    }

    @Override
    public boolean hasNext() {
        return iterator.hasNext();
    }

    @Override
    public long nextLong() {
        return mapper.applyAsLong(iterator.nextInt());
    }
}
