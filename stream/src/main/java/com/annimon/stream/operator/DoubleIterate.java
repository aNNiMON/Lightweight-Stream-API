package com.annimon.stream.operator;

import com.annimon.stream.function.DoubleUnaryOperator;
import com.annimon.stream.iterator.PrimitiveIterator;

public class DoubleIterate extends PrimitiveIterator.OfDouble {

    private final DoubleUnaryOperator op;
    private double current;

    public DoubleIterate(double seed, DoubleUnaryOperator f) {
        this.op = f;
        current = seed;
    }

    @Override
    public boolean hasNext() {
        return true;
    }

    @Override
    public double nextDouble() {
        final double old = current;
        current = op.applyAsDouble(current);
        return old;
    }
}
