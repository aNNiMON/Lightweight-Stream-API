package com.annimon.stream.operator;

import com.annimon.stream.PrimitiveExtIterator;
import com.annimon.stream.PrimitiveIterator;
import com.annimon.stream.function.IntPredicate;

public class IntTakeUntil extends PrimitiveExtIterator.OfInt {

    private final PrimitiveIterator.OfInt iterator;
    private final IntPredicate stopPredicate;

    public IntTakeUntil(PrimitiveIterator.OfInt iterator, IntPredicate stopPredicate) {
        this.iterator = iterator;
        this.stopPredicate = stopPredicate;
    }

    @Override
    protected void nextIteration() {
        hasNext = iterator.hasNext() && !(isInit && stopPredicate.test(next));
        if (hasNext) {
            next = iterator.next();
        }
    }
}
