package com.annimon.stream.operator;

import com.annimon.stream.IntStream;
import com.annimon.stream.PrimitiveIterator;
import com.annimon.stream.function.IntFunction;
import java.util.NoSuchElementException;

public class IntFlatMap extends PrimitiveIterator.OfInt {

    private final PrimitiveIterator.OfInt iterator;
    private final IntFunction<? extends IntStream> mapper;
    private PrimitiveIterator.OfInt inner;

    public IntFlatMap(PrimitiveIterator.OfInt iterator, IntFunction<? extends IntStream> mapper) {
        this.iterator = iterator;
        this.mapper = mapper;
    }

    @Override
    public boolean hasNext() {
        if (inner != null && inner.hasNext()) {
            return true;
        }
        while (iterator.hasNext()) {
            // TODO nextInt
            final int arg = iterator.next();
            final IntStream result = mapper.apply(arg);
            if (result == null) {
                continue;
            }
            if (result.iterator().hasNext()) {
                inner = result.iterator();
                return true;
            }
        }
        return false;
    }

    @Override
    public int nextInt() {
        if (inner == null) {
            throw new NoSuchElementException();
        }
        return inner.nextInt();
    }
}
