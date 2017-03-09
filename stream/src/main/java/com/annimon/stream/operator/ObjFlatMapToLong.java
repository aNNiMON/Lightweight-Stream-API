package com.annimon.stream.operator;

import com.annimon.stream.LongStream;
import com.annimon.stream.function.Function;
import com.annimon.stream.iterator.PrimitiveExtIterator;
import com.annimon.stream.iterator.PrimitiveIterator;
import java.util.Iterator;

public class ObjFlatMapToLong<T> extends PrimitiveExtIterator.OfLong {

    private final Iterator<? extends T> iterator;
    private final Function<? super T, ? extends LongStream> mapper;
    private PrimitiveIterator.OfLong inner;

    public ObjFlatMapToLong(Iterator<? extends T> iterator,
            Function<? super T, ? extends LongStream> mapper) {
        this.iterator = iterator;
        this.mapper = mapper;
    }

    @Override
    protected void nextIteration() {
        if ((inner != null) && inner.hasNext()) {
            next = inner.next();
            hasNext = true;
            return;
        }
        while (iterator.hasNext()) {
            if (inner == null || !inner.hasNext()) {
                final T arg = iterator.next();
                final LongStream result = mapper.apply(arg);
                if (result != null) {
                    inner = result.iterator();
                }
            }
            if ((inner != null) && inner.hasNext()) {
                next = inner.next();
                hasNext = true;
                return;
            }
        }
        hasNext = false;
    }
}
