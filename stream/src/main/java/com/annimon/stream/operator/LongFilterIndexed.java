package com.annimon.stream.operator;

import com.annimon.stream.function.IndexedLongPredicate;
import com.annimon.stream.iterator.PrimitiveIndexedIterator;
import com.annimon.stream.iterator.PrimitiveIterator;
import java.util.NoSuchElementException;

public class LongFilterIndexed extends PrimitiveIterator.OfLong {

    private final PrimitiveIndexedIterator.OfLong iterator;
    private final IndexedLongPredicate predicate;
    private boolean hasNext, hasNextEvaluated;
    private long next;

    public LongFilterIndexed(PrimitiveIndexedIterator.OfLong iterator, IndexedLongPredicate predicate) {
        this.iterator = iterator;
        this.predicate = predicate;
    }

    @Override
    public boolean hasNext() {
        if (!hasNextEvaluated) {
            nextIteration();
            hasNextEvaluated = true;
        }
        return hasNext;
    }

    @Override
    public long nextLong() {
        if (!hasNextEvaluated) {
            hasNext = hasNext();
        }
        if (!hasNext) {
            throw new NoSuchElementException();
        }
        hasNextEvaluated = false;
        return next;
    }

    private void nextIteration() {
        while (iterator.hasNext()) {
            final int index = iterator.getIndex();
            next = iterator.next();
            if (predicate.test(index, next)) {
                hasNext = true;
                return;
            }
        }
        hasNext = false;
    }
}
