package com.annimon.stream.operator;

import com.annimon.stream.function.Predicate;
import java.util.Iterator;
import java.util.NoSuchElementException;
import org.jetbrains.annotations.NotNull;

public class ObjTakeWhile<T> implements Iterator<T> {

    private final Iterator<? extends T> iterator;
    private final Predicate<? super T> predicate;
    private T next;
    private boolean nextPresent;
    private boolean hasNextComputed, hasNext;

    public ObjTakeWhile(
            @NotNull Iterator<? extends T> iterator,
            @NotNull Predicate<? super T> predicate) {
        this.iterator = iterator;
        this.predicate = predicate;
    }

    @Override
    public void remove() {
        throw new UnsupportedOperationException("remove not supported");
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
    public T next() {
        if (hasNextComputed && !hasNext) {
            next = null;
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
            next = null;
            hasNextComputed = true;
            throw new NoSuchElementException();
        }
    }

    private boolean getNextAndTest() {
        next = iterator.next();
        hasNext = predicate.test(next);
        return hasNext;
    }
}
