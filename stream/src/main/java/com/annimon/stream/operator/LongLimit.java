package com.annimon.stream.operator;

import com.annimon.stream.iterator.PrimitiveIterator;

public class LongLimit extends PrimitiveIterator.OfLong {

    private final PrimitiveIterator.OfLong iterator;
    private final long maxSize;
    private long index;

    public LongLimit(PrimitiveIterator.OfLong iterator, long maxSize) {
        this.iterator = iterator;
        this.maxSize = maxSize;
        index = 0;
    }

    @Override
    public boolean hasNext() {
        return (index < maxSize) && iterator.hasNext();
    }

    @Override
    public long nextLong() {
        index++;
        return iterator.nextLong();
    }
}
