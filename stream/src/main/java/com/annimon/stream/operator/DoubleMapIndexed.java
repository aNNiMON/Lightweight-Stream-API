package com.annimon.stream.operator;

import com.annimon.stream.function.IndexedDoubleUnaryOperator;
import com.annimon.stream.iterator.PrimitiveIndexedIterator;
import com.annimon.stream.iterator.PrimitiveIterator;

public class DoubleMapIndexed extends PrimitiveIterator.OfDouble {

    private final PrimitiveIndexedIterator.OfDouble iterator;
    private final IndexedDoubleUnaryOperator mapper;

    public DoubleMapIndexed(PrimitiveIndexedIterator.OfDouble iterator, IndexedDoubleUnaryOperator mapper) {
        this.iterator = iterator;
        this.mapper = mapper;
    }

    @Override
    public boolean hasNext() {
        return iterator.hasNext();
    }

    @Override
    public double nextDouble() {
        return mapper.applyAsDouble(iterator.getIndex(), iterator.next());
    }
}
