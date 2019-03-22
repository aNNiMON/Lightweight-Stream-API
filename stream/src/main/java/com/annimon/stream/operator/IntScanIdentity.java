package com.annimon.stream.operator;

import com.annimon.stream.function.IntBinaryOperator;
import com.annimon.stream.iterator.PrimitiveExtIterator;
import com.annimon.stream.iterator.PrimitiveIterator;
import org.jetbrains.annotations.NotNull;

public class IntScanIdentity extends PrimitiveExtIterator.OfInt {

    private final PrimitiveIterator.OfInt iterator;
    private final int identity;
    private final IntBinaryOperator accumulator;

    public IntScanIdentity(
            @NotNull PrimitiveIterator.OfInt iterator,
            int identity,
            @NotNull IntBinaryOperator accumulator) {
        this.iterator = iterator;
        this.identity = identity;
        this.accumulator = accumulator;
    }

    @Override
    protected void nextIteration() {
        if (!isInit) {
            // Return identity
            hasNext = true;
            next = identity;
            return;
        }
        hasNext = iterator.hasNext();
        if (hasNext) {
            final int current = iterator.next();
            next = accumulator.applyAsInt(next, current);
        }
    }
}
