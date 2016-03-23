package com.annimon.stream;

import java.util.Iterator;

class LazyIterator<T> implements Iterator<T> {
    private final Iterable<? extends T> iterable;
    private Iterator<? extends T> iterator;

    public LazyIterator(Iterable<? extends T> iterable) {
        this.iterable = iterable;
    }

    private void ensureIterator() {
        if (iterator != null) {
            return;
        }
        // Lazily creates Iterator object.
        iterator = iterable.iterator();
    }

    @Override
    public boolean hasNext() {
        ensureIterator();
        return iterator.hasNext();
    }

    @Override
    public T next() {
        ensureIterator();
        return iterator.next();
    }

    @Override
    public void remove() {
        ensureIterator();
        iterator.remove();
    }
}
