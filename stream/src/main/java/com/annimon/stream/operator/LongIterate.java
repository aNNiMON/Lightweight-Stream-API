package com.annimon.stream.operator;

import com.annimon.stream.function.LongUnaryOperator;
import com.annimon.stream.iterator.PrimitiveIterator;

public class LongIterate extends PrimitiveIterator.OfLong {

    private final LongUnaryOperator op;
    private long current;

    public LongIterate(long seed, LongUnaryOperator f) {
        this.op = f;
        current = seed;
    }

    @Override
    public boolean hasNext() {
        return true;
    }

    @Override
    public long nextLong() {
        final long old = current;
        current = op.applyAsLong(current);
        return old;
    }
}
