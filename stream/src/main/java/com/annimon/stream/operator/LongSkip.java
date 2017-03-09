package com.annimon.stream.operator;

import com.annimon.stream.iterator.PrimitiveIterator;

public class LongSkip extends PrimitiveIterator.OfLong {

    private final PrimitiveIterator.OfLong iterator;
    private final long n;
    private long skipped;

    public LongSkip(PrimitiveIterator.OfLong iterator, long n) {
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
            iterator.nextLong();
            skipped++;
        }
        return iterator.hasNext();
    }

    @Override
    public long nextLong() {
        return iterator.nextLong();
    }
}
