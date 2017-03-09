package com.annimon.stream.operator;

import com.annimon.stream.function.IndexedPredicate;
import com.annimon.stream.iterator.IndexedIterator;
import com.annimon.stream.iterator.LsaExtIterator;

public class ObjTakeUntilIndexed<T> extends LsaExtIterator<T> {

    private final IndexedIterator<? extends T> iterator;
    private final IndexedPredicate<? super T> stopPredicate;

    public ObjTakeUntilIndexed(IndexedIterator<? extends T> iterator, IndexedPredicate<? super T> predicate) {
        this.iterator = iterator;
        this.stopPredicate = predicate;
    }

    @Override
    protected void nextIteration() {
        hasNext = iterator.hasNext() && !(isInit && stopPredicate.test(iterator.getIndex(), next));
        if (hasNext) {
            next = iterator.next();
        }
    }
}
