package com.annimon.stream.operator;

import com.annimon.stream.function.BiFunction;
import com.annimon.stream.iterator.LsaExtIterator;
import java.util.Iterator;

public class ObjScanIdentity<T, R> extends LsaExtIterator<R> {

    private final Iterator<? extends T> iterator;
    private final R identity;
    private final BiFunction<? super R, ? super T, ? extends R> accumulator;

    public ObjScanIdentity(Iterator<? extends T> iterator, R identity,
            BiFunction<? super R, ? super T, ? extends R> accumulator) {
        this.iterator = iterator;
        this.identity = identity;
        this.accumulator = accumulator;
    }

    @Override
    protected void nextIteration() {
        if (!isInit) {
            // Return identity
            hasNext = true;
            next = identity;
            return;
        }
        hasNext = iterator.hasNext();
        if (hasNext) {
            final T t = iterator.next();
            next = accumulator.apply(next, t);
        }
    }
}
