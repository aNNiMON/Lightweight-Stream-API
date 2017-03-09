package com.annimon.stream.operator;

import com.annimon.stream.function.LongSupplier;
import com.annimon.stream.iterator.PrimitiveIterator;

public class LongGenerate extends PrimitiveIterator.OfLong {

    private final LongSupplier supplier;

    public LongGenerate(LongSupplier supplier) {
        this.supplier = supplier;
    }

    @Override
    public boolean hasNext() {
        return true;
    }

    @Override
    public long nextLong() {
        return supplier.getAsLong();
    }
}
