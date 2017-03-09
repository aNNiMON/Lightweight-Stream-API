package com.annimon.stream.operator;

import com.annimon.stream.iterator.PrimitiveIterator;

public class IntLimit extends PrimitiveIterator.OfInt {

    private final PrimitiveIterator.OfInt iterator;
    private final long maxSize;
    private long index;

    public IntLimit(PrimitiveIterator.OfInt iterator, long maxSize) {
        this.iterator = iterator;
        this.maxSize = maxSize;
        index = 0;
    }

    @Override
    public boolean hasNext() {
        return (index < maxSize) && iterator.hasNext();
    }

    @Override
    public int nextInt() {
        index++;
        return iterator.nextInt();
    }
}
