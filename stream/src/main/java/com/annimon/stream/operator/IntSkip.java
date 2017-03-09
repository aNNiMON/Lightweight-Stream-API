package com.annimon.stream.operator;

import com.annimon.stream.iterator.PrimitiveIterator;

public class IntSkip extends PrimitiveIterator.OfInt {

    private final PrimitiveIterator.OfInt iterator;
    private final long n;
    private long skipped;

    public IntSkip(PrimitiveIterator.OfInt iterator, long n) {
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
            iterator.nextInt();
            skipped++;
        }

        return iterator.hasNext();
    }

    @Override
    public int nextInt() {
        return iterator.nextInt();
    }
}
