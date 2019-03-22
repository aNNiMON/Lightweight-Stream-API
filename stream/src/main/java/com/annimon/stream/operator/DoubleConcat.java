package com.annimon.stream.operator;

import com.annimon.stream.iterator.PrimitiveIterator;
import org.jetbrains.annotations.NotNull;

public class DoubleConcat extends PrimitiveIterator.OfDouble {

    private final PrimitiveIterator.OfDouble iterator1;
    private final PrimitiveIterator.OfDouble iterator2;
    private boolean firstStreamIsCurrent;

    public DoubleConcat(
            @NotNull PrimitiveIterator.OfDouble iterator1,
            @NotNull PrimitiveIterator.OfDouble iterator2) {
        this.iterator1 = iterator1;
        this.iterator2 = iterator2;
        firstStreamIsCurrent = true;
    }

    @Override
    public boolean hasNext() {
        if (firstStreamIsCurrent) {
            if (iterator1.hasNext()) {
                return true;
            }
            firstStreamIsCurrent = false;
        }
        return iterator2.hasNext();
    }

    @Override
    public double nextDouble() {
        return firstStreamIsCurrent ? iterator1.nextDouble() : iterator2.nextDouble();
    }
}
