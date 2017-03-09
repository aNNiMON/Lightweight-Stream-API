package com.annimon.stream.operator;

import com.annimon.stream.function.IntConsumer;
import com.annimon.stream.iterator.PrimitiveIterator;

public class IntPeek extends PrimitiveIterator.OfInt {

    private final PrimitiveIterator.OfInt iterator;
    private final IntConsumer action;

    public IntPeek(PrimitiveIterator.OfInt iterator, IntConsumer action) {
        this.iterator = iterator;
        this.action = action;
    }

    @Override
    public boolean hasNext() {
        return iterator.hasNext();
    }

    @Override
    public int nextInt() {
        final int value = iterator.nextInt();
        action.accept(value);
        return value;
    }
}
