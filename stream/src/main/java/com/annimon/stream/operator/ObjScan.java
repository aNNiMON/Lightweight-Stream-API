package com.annimon.stream.operator;

import com.annimon.stream.function.BiFunction;
import com.annimon.stream.iterator.LsaExtIterator;
import java.util.Iterator;
import org.jetbrains.annotations.NotNull;

public class ObjScan<T> extends LsaExtIterator<T> {

    private final Iterator<? extends T> iterator;
    private final BiFunction<T, T, T> accumulator;

    public ObjScan(
            @NotNull Iterator<? extends T> iterator,
            @NotNull BiFunction<T, T, T> accumulator) {
        this.iterator = iterator;
        this.accumulator = accumulator;
    }

    @Override
    protected void nextIteration() {
        hasNext = iterator.hasNext();
        if (hasNext) {
            final T value = iterator.next();
            if (isInit) {
                next = accumulator.apply(next, value);
            } else {
                next = value;
            }
        }
    }
}
