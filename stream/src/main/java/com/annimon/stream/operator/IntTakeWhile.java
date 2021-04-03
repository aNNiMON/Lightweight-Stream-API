package com.annimon.stream.operator;

import com.annimon.stream.function.IntPredicate;
import com.annimon.stream.iterator.PrimitiveIterator;
import java.util.NoSuchElementException;
import org.jetbrains.annotations.NotNull;

public class IntTakeWhile extends PrimitiveIterator.OfInt {

    private final PrimitiveIterator.OfInt iterator;
    private final IntPredicate predicate;
    private int next;
    private boolean nextPresent;
    private boolean hasNextComputed, hasNext;

    public IntTakeWhile(
            @NotNull PrimitiveIterator.OfInt iterator,
            @NotNull IntPredicate predicate) {
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
    public int nextInt() {
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
        next = iterator.nextInt();
        hasNext = predicate.test(next);
        return hasNext;
    }
}

