package com.annimon.stream.operator;

import com.annimon.stream.iterator.PrimitiveIterator;

public class DoubleSample extends PrimitiveIterator.OfDouble {

    private final PrimitiveIterator.OfDouble iterator;
    private final int stepWidth;

    public DoubleSample(PrimitiveIterator.OfDouble iterator, int stepWidth) {
        this.iterator = iterator;
        this.stepWidth = stepWidth;
    }

    @Override
    public boolean hasNext() {
        return iterator.hasNext();
    }

    @Override
    public double nextDouble() {
        final double result = iterator.nextDouble();
        int skip = 1;
        while (skip < stepWidth && iterator.hasNext()) {
            iterator.nextDouble();
            skip++;
        }
        return result;
    }
}
