package com.annimon.stream.operator;

import com.annimon.stream.iterator.LsaIterator;
import java.util.Iterator;

public class ObjLimit<T> extends LsaIterator<T> {

    private final Iterator<? extends T> iterator;
    private final long maxSize;
    private long index;

    public ObjLimit(Iterator<? extends T> iterator, long maxSize) {
        this.iterator = iterator;
        this.maxSize = maxSize;
        index = 0;
    }

    @Override
    public boolean hasNext() {
        return (index < maxSize) && iterator.hasNext();
    }

    @Override
    public T nextIteration() {
        index++;
        return iterator.next();
    }
}
