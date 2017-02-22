package com.annimon.stream.operator;

import com.annimon.stream.LongStream;
import com.annimon.stream.PrimitiveIterator;
import com.annimon.stream.function.LongFunction;
import java.util.NoSuchElementException;

public class LongFlatMap extends PrimitiveIterator.OfLong {

    private final PrimitiveIterator.OfLong iterator;
    private final LongFunction<? extends LongStream> mapper;
    private PrimitiveIterator.OfLong inner;

    public LongFlatMap(PrimitiveIterator.OfLong iterator, LongFunction<? extends LongStream> mapper) {
        this.iterator = iterator;
        this.mapper = mapper;
    }

    @Override
    public boolean hasNext() {
        if (inner != null && inner.hasNext()) {
            return true;
        }
        while (iterator.hasNext()) {
            // TODO nextLong
            final long arg = iterator.next();
            final LongStream result = mapper.apply(arg);
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
    public long nextLong() {
        if (inner == null) {
            throw new NoSuchElementException();
        }
        return inner.nextLong();
    }
}
