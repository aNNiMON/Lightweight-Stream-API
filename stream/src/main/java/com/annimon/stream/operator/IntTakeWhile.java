package com.annimon.stream.operator;

import com.annimon.stream.PrimitiveExtIterator;
import com.annimon.stream.PrimitiveIterator;
import com.annimon.stream.function.IntPredicate;

public class IntTakeWhile extends PrimitiveExtIterator.OfInt {

    private final PrimitiveIterator.OfInt iterator;
    private final IntPredicate predicate;

    public IntTakeWhile(PrimitiveIterator.OfInt iterator, IntPredicate predicate) {
        this.iterator = iterator;
        this.predicate = predicate;
    }

    @Override
    protected void nextIteration() {
        hasNext = iterator.hasNext()
                && predicate.test(next = iterator.next());
    }
}
