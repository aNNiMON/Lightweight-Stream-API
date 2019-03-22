package com.annimon.stream.operator;

import com.annimon.stream.iterator.PrimitiveIterator;
import org.jetbrains.annotations.NotNull;

public class IntConcat extends PrimitiveIterator.OfInt {

    private final PrimitiveIterator.OfInt iterator1;
    private final PrimitiveIterator.OfInt iterator2;
    private boolean firstStreamIsCurrent;

    public IntConcat(
            @NotNull PrimitiveIterator.OfInt iterator1,
            @NotNull PrimitiveIterator.OfInt iterator2) {
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
    public int nextInt() {
        return firstStreamIsCurrent ? iterator1.nextInt() : iterator2.nextInt();
    }
}
