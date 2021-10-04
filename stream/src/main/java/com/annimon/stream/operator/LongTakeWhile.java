package com.annimon.stream.operator;

import com.annimon.stream.function.LongPredicate;
import com.annimon.stream.iterator.PrimitiveIterator;
import java.util.NoSuchElementException;
import org.jetbrains.annotations.NotNull;

public class LongTakeWhile extends PrimitiveIterator.OfLong {

    private final PrimitiveIterator.OfLong iterator;
    private final LongPredicate predicate;
    private long next;
    private boolean nextPresent;
    private boolean hasNextComputed, hasNext;

    public LongTakeWhile(
            @NotNull PrimitiveIterator.OfLong iterator, @NotNull LongPredicate predicate) {
        this.iterator = iterator;
        this.predicate = predicate;
    }

    @Override
    public boolean hasNext() {
        if (hasNextComputed) {
            return hasNext;
        }
        hasNext = iterator.hasNext();
        hasNextComputed = true;
        if (hasNext) {
            // Retrieve and cache next element for further next() operation
            nextPresent = getNextAndTest();
        }
        return hasNext;
    }

    @Override
    public long nextLong() {
        if (hasNextComputed && !hasNext) {
            throw new NoSuchElementException();
        }
        hasNextComputed = false;
        if (nextPresent) {
            // Return cached value that was previously retrieved in hasNext()
            nextPresent = false;
            return next;
        }
        if (getNextAndTest()) {
            return next;
        } else {
            hasNextComputed = true;
            throw new NoSuchElementException();
        }
    }

    private boolean getNextAndTest() {
        next = iterator.nextLong();
        hasNext = predicate.test(next);
        return hasNext;
    }
}
