package com.annimon.stream.operator;

import com.annimon.stream.function.BiFunction;
import com.annimon.stream.iterator.LsaExtIterator;
import java.util.Iterator;

public class ObjMerge<T> extends LsaExtIterator<T> {

    public enum MergeResult {
        FIRST, SECOND, SKIP;
    }

    private final Iterator<? extends T> iterator1;
    private final Iterator<? extends T> iterator2;
    private final BiFunction<? super T, ? super T, MergeResult> selector;

    public ObjMerge(Iterator<? extends T> iterator1, Iterator<? extends T> iterator2, BiFunction<? super T, ? super T, MergeResult> selector) {
        this.iterator1 = iterator1;
        this.iterator2 = iterator2;
        this.selector = selector;
    }

    @Override
    protected void nextIteration() {
        while (iterator1.hasNext() && iterator2.hasNext()) {
            final T v1 = iterator1.next();
            final T v2 = iterator2.next();
            final MergeResult result = selector.apply(v1, v2);
            switch (result) {
                case FIRST:
                    next = v1;
                    hasNext = true;
                    return;
                case SECOND:
                    next = v2;
                    hasNext = true;
                    return;
                case SKIP:
                    // skip to the next iteration
            }
        }
        hasNext = false;
    }
}
