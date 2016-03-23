package com.annimon.stream;

import java.util.Iterator;
import java.util.NoSuchElementException;

public abstract class LsaExtIterator<T> implements Iterator<T> {

    protected T next;
    protected boolean hasNext, isInit;

    @Override
    public boolean hasNext() {
        if (!isInit) {
            nextIteration();
            isInit = true;
        }
        return hasNext;
    }

    @Override
    public T next() {
        if (!hasNext) {
            throw new NoSuchElementException();
        }
        final T result = next;
        nextIteration();
        return result;
    }

    protected abstract void nextIteration();

    @Override
    public void remove() {
        throw new UnsupportedOperationException("remove not supported");
    }
}