package com.annimon.stream.operator;

import com.annimon.stream.function.Predicate;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class ObjFilter<T> implements Iterator<T> {

    private final Iterator<? extends T> iterator;
    private final Predicate<? super T> predicate;
    private boolean hasNext, hasNextEvaluated;
    private T next;

    public ObjFilter(Iterator<? extends T> iterator, Predicate<? super T> predicate) {
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
    public T next() {
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
            next = iterator.next();
            if (predicate.test(next)) {
                hasNext = true;
                return;
            }
        }
        hasNext = false;
    }

    @Override
    public void remove() {
        throw new UnsupportedOperationException("remove not supported");
    }
}
