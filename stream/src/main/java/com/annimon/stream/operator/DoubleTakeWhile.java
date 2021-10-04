package com.annimon.stream.operator;

import com.annimon.stream.function.DoublePredicate;
import com.annimon.stream.iterator.PrimitiveIterator;
import java.util.NoSuchElementException;
import org.jetbrains.annotations.NotNull;

public class DoubleTakeWhile extends PrimitiveIterator.OfDouble {

    private final PrimitiveIterator.OfDouble iterator;
    private final DoublePredicate predicate;
    private double next;
    private boolean nextPresent;
    private boolean hasNextComputed, hasNext;

    public DoubleTakeWhile(
            @NotNull PrimitiveIterator.OfDouble iterator, @NotNull DoublePredicate predicate) {
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
    public double nextDouble() {
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
        next = iterator.nextDouble();
        hasNext = predicate.test(next);
        return hasNext;
    }
}
