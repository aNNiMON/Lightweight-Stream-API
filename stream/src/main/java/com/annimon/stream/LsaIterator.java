package com.annimon.stream;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Abstract iterator with implemented {@code remove} method.
 */
public abstract class LsaIterator<T> implements Iterator<T> {
 
    @Override
    public void remove() {
        throw new UnsupportedOperationException("remove not supported");
    }

    @Override
    public final T next() {
        if (!hasNext()) {
            throw new NoSuchElementException();
        }
        return nextIteration();
    }

    public abstract T nextIteration();
}
