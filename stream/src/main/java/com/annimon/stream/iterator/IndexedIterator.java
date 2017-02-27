package com.annimon.stream.iterator;

import java.util.Iterator;

/**
 * Iterator wrapper that supports indexing.
 *
 * @param <T> the type of the iterator elements
 * @since 1.1.6
 */
public class IndexedIterator<T> implements Iterator<T> {

    private final Iterator<? extends T> iterator;
    private final int step;
    private int index;

    public IndexedIterator(Iterator<? extends T> iterator) {
        this(0, 1, iterator);
    }

    public IndexedIterator(int start, int step, Iterator<? extends T> iterator) {
        this.iterator = iterator;
        this.step = step;
        index = start;
    }

    public int getIndex() {
        return index;
    }

    @Override
    public boolean hasNext() {
        return iterator.hasNext();
    }

    @Override
    public T next() {
        final T result = iterator.next();
        index += step;
        return result;
    }

    @Override
    public void remove() {
        iterator.remove();
    }
}
