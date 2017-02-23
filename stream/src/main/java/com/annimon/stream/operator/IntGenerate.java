package com.annimon.stream.operator;

import com.annimon.stream.PrimitiveIterator;
import com.annimon.stream.function.IntSupplier;

public class IntGenerate extends PrimitiveIterator.OfInt {

    private final IntSupplier supplier;

    public IntGenerate(IntSupplier supplier) {
        this.supplier = supplier;
    }

    @Override
    public boolean hasNext() {
        return true;
    }

    @Override
    public int nextInt() {
        return supplier.getAsInt();
    }
}
