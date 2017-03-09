package com.annimon.stream.operator;

import com.annimon.stream.function.IntPredicate;
import com.annimon.stream.iterator.PrimitiveIterator;

public class IntFilter extends PrimitiveIterator.OfInt {

    private final PrimitiveIterator.OfInt iterator;
    private final IntPredicate predicate;
    private int next;

    public IntFilter(PrimitiveIterator.OfInt iterator, IntPredicate predicate) {
        this.iterator = iterator;
        this.predicate = predicate;
    }

    @Override
    public boolean hasNext() {
        while (iterator.hasNext()) {
            next = iterator.nextInt();
            if (predicate.test(next)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public int nextInt() {
        return next;
    }
}
