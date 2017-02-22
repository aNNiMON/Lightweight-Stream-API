package com.annimon.stream.operator;

import com.annimon.stream.PrimitiveExtIterator;
import com.annimon.stream.PrimitiveIterator;
import com.annimon.stream.function.LongPredicate;

public class LongTakeWhile extends PrimitiveExtIterator.OfLong {

    private final PrimitiveIterator.OfLong iterator;
    private final LongPredicate predicate;

    public LongTakeWhile(PrimitiveIterator.OfLong iterator, LongPredicate predicate) {
        this.iterator = iterator;
        this.predicate = predicate;
    }

    @Override
    protected void nextIteration() {
        hasNext = iterator.hasNext()
                && predicate.test(next = iterator.next());
    }
}
