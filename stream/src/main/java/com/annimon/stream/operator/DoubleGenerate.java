package com.annimon.stream.operator;

import com.annimon.stream.function.DoubleSupplier;
import com.annimon.stream.iterator.PrimitiveIterator;

public class DoubleGenerate extends PrimitiveIterator.OfDouble {

    private final DoubleSupplier supplier;

    public DoubleGenerate(DoubleSupplier supplier) {
        this.supplier = supplier;
    }

    @Override
    public boolean hasNext() {
        return true;
    }

    @Override
    public double nextDouble() {
        return supplier.getAsDouble();
    }
}
