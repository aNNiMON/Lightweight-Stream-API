package com.annimon.stream.operator;

import com.annimon.stream.PrimitiveExtIterator;
import com.annimon.stream.PrimitiveIterator;
import com.annimon.stream.function.LongBinaryOperator;

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
            // TODO nextLong
            final long current = iterator.next();
            if (isInit) {
                next = accumulator.applyAsLong(next, current);
            } else {
                next = current;
            }
        }
    }
}
