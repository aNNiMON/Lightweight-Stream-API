package com.annimon.stream.operator;

import com.annimon.stream.DoubleStream;
import com.annimon.stream.PrimitiveIterator;
import com.annimon.stream.function.DoubleFunction;
import java.util.NoSuchElementException;

public class DoubleFlatMap extends PrimitiveIterator.OfDouble {

    private final PrimitiveIterator.OfDouble iterator;
    private final DoubleFunction<? extends DoubleStream> mapper;
    private PrimitiveIterator.OfDouble inner;

    public DoubleFlatMap(PrimitiveIterator.OfDouble iterator, DoubleFunction<? extends DoubleStream> mapper) {
        this.iterator = iterator;
        this.mapper = mapper;
    }

    @Override
    public boolean hasNext() {
        if (inner != null && inner.hasNext()) {
            return true;
        }
        while (iterator.hasNext()) {
            final double arg = iterator.nextDouble();
            final DoubleStream result = mapper.apply(arg);
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
    public double nextDouble() {
        if (inner == null) {
            throw new NoSuchElementException();
        }
        return inner.nextDouble();
    }
}
