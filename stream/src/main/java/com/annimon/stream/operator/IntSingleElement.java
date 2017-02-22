package com.annimon.stream.operator;

import com.annimon.stream.PrimitiveIterator;

public class IntSingleElement extends PrimitiveIterator.OfInt {

    private final int element;
    private boolean hasNext;

    public IntSingleElement(int element) {
        this.element = element;
        hasNext = true;
    }

    @Override
    public boolean hasNext() {
        return hasNext;
    }

    @Override
    public int nextInt() {
        hasNext = false;
        return element;
    }
}
