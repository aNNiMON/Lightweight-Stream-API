package com.annimon.stream.operator;

import com.annimon.stream.PrimitiveIterator;

public class LongSingleElement extends PrimitiveIterator.OfLong {

    private final long element;
    private boolean hasNext;

    public LongSingleElement(long element) {
        this.element = element;
        hasNext = true;
    }

    @Override
    public boolean hasNext() {
        return hasNext;
    }

    @Override
    public long nextLong() {
        hasNext = false;
        return element;
    }
}
