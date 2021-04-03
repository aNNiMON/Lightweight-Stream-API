package com.annimon.stream.operator;

import com.annimon.stream.function.IndexedPredicate;
import com.annimon.stream.iterator.IndexedIterator;
import java.util.Iterator;
import java.util.NoSuchElementException;
import org.jetbrains.annotations.NotNull;

public class ObjTakeWhileIndexed<T> implements Iterator<T> {

    private final IndexedIterator<? extends T> iterator;
    private final IndexedPredicate<? super T> predicate;
    private T next;
    private boolean nextPresent;
    private boolean hasNextComputed, hasNext;

    public ObjTakeWhileIndexed(
            @NotNull IndexedIterator<? extends T> iterator,
            @NotNull IndexedPredicate<? super T> predicate) {
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
        int nextIndex = iterator.getIndex();
        next = iterator.next();
        hasNext = predicate.test(nextIndex, next);
        return hasNext;
    }
}
