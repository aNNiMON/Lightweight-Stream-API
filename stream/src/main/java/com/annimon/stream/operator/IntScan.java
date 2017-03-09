package com.annimon.stream.operator;

import com.annimon.stream.function.IntBinaryOperator;
import com.annimon.stream.iterator.PrimitiveExtIterator;
import com.annimon.stream.iterator.PrimitiveIterator;

public class IntScan extends PrimitiveExtIterator.OfInt {

    private final PrimitiveIterator.OfInt iterator;
    private final IntBinaryOperator accumulator;

    public IntScan(PrimitiveIterator.OfInt iterator, IntBinaryOperator accumulator) {
        this.iterator = iterator;
        this.accumulator = accumulator;
    }

    @Override
    protected void nextIteration() {
        hasNext = iterator.hasNext();
        if (hasNext) {
            final int current = iterator.next();
            if (isInit) {
                next = accumulator.applyAsInt(next, current);
            } else {
                next = current;
            }
        }
    }
}
