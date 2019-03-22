package com.annimon.stream.operator;

import com.annimon.stream.function.IntPredicate;
import com.annimon.stream.iterator.PrimitiveExtIterator;
import com.annimon.stream.iterator.PrimitiveIterator;
import org.jetbrains.annotations.NotNull;

public class IntTakeUntil extends PrimitiveExtIterator.OfInt {

    private final PrimitiveIterator.OfInt iterator;
    private final IntPredicate stopPredicate;

    public IntTakeUntil(
            @NotNull PrimitiveIterator.OfInt iterator,
            @NotNull IntPredicate stopPredicate) {
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
