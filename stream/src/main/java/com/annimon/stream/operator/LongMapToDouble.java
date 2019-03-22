package com.annimon.stream.operator;

import com.annimon.stream.function.LongToDoubleFunction;
import com.annimon.stream.iterator.PrimitiveIterator;
import org.jetbrains.annotations.NotNull;

public class LongMapToDouble extends PrimitiveIterator.OfDouble {

    private final PrimitiveIterator.OfLong iterator;
    private final LongToDoubleFunction mapper;

    public LongMapToDouble(
            @NotNull PrimitiveIterator.OfLong iterator,
            @NotNull LongToDoubleFunction mapper) {
        this.iterator = iterator;
        this.mapper = mapper;
    }

    @Override
    public boolean hasNext() {
        return iterator.hasNext();
    }

    @Override
    public double nextDouble() {
        return mapper.applyAsDouble(iterator.nextLong());
    }
}
