package com.annimon.stream.operator;

import com.annimon.stream.function.Predicate;
import com.annimon.stream.iterator.LsaExtIterator;
import java.util.Iterator;
import org.jetbrains.annotations.NotNull;

public class ObjTakeUntil<T> extends LsaExtIterator<T> {

    private final Iterator<? extends T> iterator;
    private final Predicate<? super T> stopPredicate;

    public ObjTakeUntil(
            @NotNull Iterator<? extends T> iterator,
            @NotNull Predicate<? super T> predicate) {
        this.iterator = iterator;
        this.stopPredicate = predicate;
    }

    @Override
    protected void nextIteration() {
        hasNext = iterator.hasNext() && !(isInit && stopPredicate.test(next));
        if (hasNext) {
            next = iterator.next();
        }
    }
}
