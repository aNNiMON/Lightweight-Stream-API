package com.annimon.stream.operator;

import com.annimon.stream.function.Predicate;
import com.annimon.stream.iterator.LsaExtIterator;
import java.util.Iterator;
import org.jetbrains.annotations.NotNull;

public class ObjDropWhile<T> extends LsaExtIterator<T> {

    private final Iterator<? extends T> iterator;
    private final Predicate<? super T> predicate;

    public ObjDropWhile(
            @NotNull Iterator<? extends T> iterator, @NotNull Predicate<? super T> predicate) {
        this.iterator = iterator;
        this.predicate = predicate;
    }

    @Override
    protected void nextIteration() {
        if (!isInit) {
            // Skip first time
            while (hasNext = iterator.hasNext()) {
                next = iterator.next();
                if (!predicate.test(next)) {
                    return;
                }
            }
        }

        hasNext = hasNext && iterator.hasNext();
        if (!hasNext) return;

        next = iterator.next();
    }
}
