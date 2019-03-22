package com.annimon.stream.operator;

import com.annimon.stream.function.IntBinaryOperator;
import com.annimon.stream.iterator.PrimitiveIndexedIterator;
import com.annimon.stream.iterator.PrimitiveIterator;
import org.jetbrains.annotations.NotNull;

public class IntMapIndexed extends PrimitiveIterator.OfInt {

    private final PrimitiveIndexedIterator.OfInt iterator;
    private final IntBinaryOperator mapper;

    public IntMapIndexed(
            @NotNull PrimitiveIndexedIterator.OfInt iterator,
            @NotNull IntBinaryOperator mapper) {
        this.iterator = iterator;
        this.mapper = mapper;
    }

    @Override
    public boolean hasNext() {
        return iterator.hasNext();
    }

    @Override
    public int nextInt() {
        return mapper.applyAsInt(iterator.getIndex(), iterator.next());
    }
}
