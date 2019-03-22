package com.annimon.stream.operator;

import com.annimon.stream.function.DoublePredicate;
import com.annimon.stream.iterator.PrimitiveExtIterator;
import com.annimon.stream.iterator.PrimitiveIterator;
import org.jetbrains.annotations.NotNull;

public class DoubleTakeUntil extends PrimitiveExtIterator.OfDouble {

    private final PrimitiveIterator.OfDouble iterator;
    private final DoublePredicate stopPredicate;

    public DoubleTakeUntil(
            @NotNull PrimitiveIterator.OfDouble iterator,
            @NotNull DoublePredicate stopPredicate) {
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
