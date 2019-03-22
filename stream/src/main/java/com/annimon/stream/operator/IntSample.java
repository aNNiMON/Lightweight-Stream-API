package com.annimon.stream.operator;

import com.annimon.stream.iterator.PrimitiveIterator;
import org.jetbrains.annotations.NotNull;

public class IntSample extends PrimitiveIterator.OfInt {

    private final PrimitiveIterator.OfInt iterator;
    private final int stepWidth;

    public IntSample(@NotNull PrimitiveIterator.OfInt iterator, int stepWidth) {
        this.iterator = iterator;
        this.stepWidth = stepWidth;
    }

    @Override
    public boolean hasNext() {
        return iterator.hasNext();
    }

    @Override
    public int nextInt() {
        final int result = iterator.nextInt();
        int skip = 1;
        while (skip < stepWidth && iterator.hasNext()) {
            iterator.nextInt();
            skip++;
        }
        return result;
    }
}
