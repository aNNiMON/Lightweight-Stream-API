package com.annimon.stream.operator;

import com.annimon.stream.PrimitiveIterator;
import com.annimon.stream.function.LongPredicate;

public class LongFilter extends PrimitiveIterator.OfLong {

    private final PrimitiveIterator.OfLong iterator;
    private final LongPredicate predicate;
    private long next;

    public LongFilter(PrimitiveIterator.OfLong iterator, LongPredicate predicate) {
        this.iterator = iterator;
        this.predicate = predicate;
    }

    @Override
    public boolean hasNext() {
        while (iterator.hasNext()) {
            next = iterator.nextLong();
            if (predicate.test(next)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public long nextLong() {
        return next;
    }
}
