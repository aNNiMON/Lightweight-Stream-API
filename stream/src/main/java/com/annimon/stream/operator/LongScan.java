package com.annimon.stream.operator;

import com.annimon.stream.function.LongBinaryOperator;
import com.annimon.stream.iterator.PrimitiveExtIterator;
import com.annimon.stream.iterator.PrimitiveIterator;

public class LongScan extends PrimitiveExtIterator.OfLong {

    private final PrimitiveIterator.OfLong iterator;
    private final LongBinaryOperator accumulator;

    public LongScan(PrimitiveIterator.OfLong iterator, LongBinaryOperator accumulator) {
        this.iterator = iterator;
        this.accumulator = accumulator;
    }

    @Override
    protected void nextIteration() {
        hasNext = iterator.hasNext();
        if (hasNext) {
            final long current = iterator.nextLong();
            if (isInit) {
                next = accumulator.applyAsLong(next, current);
            } else {
                next = current;
            }
        }
    }
}
