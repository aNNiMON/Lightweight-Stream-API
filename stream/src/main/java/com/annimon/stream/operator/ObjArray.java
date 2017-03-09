package com.annimon.stream.operator;

import com.annimon.stream.iterator.LsaIterator;

public class ObjArray<T> extends LsaIterator<T> {

    private final T[] elements;
    private int index;

    public ObjArray(T[] elements) {
        this.elements = elements;
        index = 0;
    }

    @Override
    public boolean hasNext() {
        return index < elements.length;
    }

    @Override
    public T nextIteration() {
        return elements[index++];
    }
}
