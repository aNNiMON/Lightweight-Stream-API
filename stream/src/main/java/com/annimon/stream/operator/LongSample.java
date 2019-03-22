package com.annimon.stream.operator;

import com.annimon.stream.iterator.PrimitiveIterator;
import org.jetbrains.annotations.NotNull;

public class LongSample extends PrimitiveIterator.OfLong {

    private final PrimitiveIterator.OfLong iterator;
    private final int stepWidth;

    public LongSample(@NotNull PrimitiveIterator.OfLong iterator, int stepWidth) {
        this.iterator = iterator;
        this.stepWidth = stepWidth;
    }

    @Override
    public boolean hasNext() {
        return iterator.hasNext();
    }

    @Override
    public long nextLong() {
        final long result = iterator.nextLong();
        int skip = 1;
        while (skip < stepWidth && iterator.hasNext()) {
            iterator.nextLong();
            skip++;
        }
        return result;
    }
}
