package com.annimon.stream.operator;

import com.annimon.stream.function.DoubleBinaryOperator;
import com.annimon.stream.iterator.PrimitiveExtIterator;
import com.annimon.stream.iterator.PrimitiveIterator;

public class DoubleScan extends PrimitiveExtIterator.OfDouble {

    private final PrimitiveIterator.OfDouble iterator;
    private final DoubleBinaryOperator accumulator;

    public DoubleScan(PrimitiveIterator.OfDouble iterator, DoubleBinaryOperator accumulator) {
        this.iterator = iterator;
        this.accumulator = accumulator;
    }

    @Override
    protected void nextIteration() {
        hasNext = iterator.hasNext();
        if (hasNext) {
            final double current = iterator.nextDouble();
            if (isInit) {
                next = accumulator.applyAsDouble(next, current);
            } else {
                next = current;
            }
        }
    }
}
