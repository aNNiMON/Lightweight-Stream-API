package com.annimon.stream.operator;

import com.annimon.stream.iterator.PrimitiveIterator;

public class IntArray extends PrimitiveIterator.OfInt {

    private final int[] values;
    private int index;

    public IntArray(int[] values) {
        this.values = values;
        index = 0;
    }

    @Override
    public boolean hasNext() {
        return index < values.length;
    }
    
    @Override
    public int nextInt() {
        return values[index++];
    }
}
