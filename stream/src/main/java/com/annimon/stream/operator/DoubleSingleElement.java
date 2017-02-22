package com.annimon.stream.operator;

import com.annimon.stream.PrimitiveIterator;

public class DoubleSingleElement extends PrimitiveIterator.OfDouble {

    private final double element;
    private boolean hasNext;

    public DoubleSingleElement(double element) {
        this.element = element;
        hasNext = true;
    }

    @Override
    public boolean hasNext() {
        return hasNext;
    }

    @Override
    public double nextDouble() {
        hasNext = false;
        return element;
    }
}
