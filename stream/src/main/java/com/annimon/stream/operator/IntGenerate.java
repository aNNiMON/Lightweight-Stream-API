package com.annimon.stream.operator;

import com.annimon.stream.function.IntSupplier;
import com.annimon.stream.iterator.PrimitiveIterator;

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
