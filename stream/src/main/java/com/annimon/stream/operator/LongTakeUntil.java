package com.annimon.stream.operator;

import com.annimon.stream.function.LongPredicate;
import com.annimon.stream.iterator.PrimitiveExtIterator;
import com.annimon.stream.iterator.PrimitiveIterator;
import org.jetbrains.annotations.NotNull;

public class LongTakeUntil extends PrimitiveExtIterator.OfLong {

    private final PrimitiveIterator.OfLong iterator;
    private final LongPredicate stopPredicate;

    public LongTakeUntil(
            @NotNull PrimitiveIterator.OfLong iterator, @NotNull LongPredicate stopPredicate) {
        this.iterator = iterator;
        this.stopPredicate = stopPredicate;
    }

    @Override
    protected void nextIteration() {
        hasNext = iterator.hasNext() && !(isInit && stopPredicate.test(next));
        if (hasNext) {
            next = iterator.nextLong();
        }
    }
}
