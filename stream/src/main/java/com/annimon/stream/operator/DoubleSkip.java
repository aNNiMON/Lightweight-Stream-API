package com.annimon.stream.operator;

import com.annimon.stream.iterator.PrimitiveIterator;

public class DoubleSkip extends PrimitiveIterator.OfDouble {

    private final PrimitiveIterator.OfDouble iterator;
    private final long n;
    private long skipped;

    public DoubleSkip(PrimitiveIterator.OfDouble iterator, long n) {
        this.iterator = iterator;
        this.n = n;
        skipped = 0;
    }

    @Override
    public boolean hasNext() {
        while (iterator.hasNext()) {
            if (skipped == n) {
                break;
            }
            iterator.nextDouble();
            skipped++;
        }
        return iterator.hasNext();
    }

    @Override
    public double nextDouble() {
        return iterator.nextDouble();
    }
}
