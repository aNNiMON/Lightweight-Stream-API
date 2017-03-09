package com.annimon.stream.operator;

import com.annimon.stream.internal.Operators;
import com.annimon.stream.iterator.LsaExtIterator;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

public class ObjSorted<T> extends LsaExtIterator<T> {

    private final Iterator<? extends T> iterator;
    private final Comparator<? super T> comparator;
    private Iterator<T> sortedIterator;

    public ObjSorted(Iterator<? extends T> iterator, Comparator<? super T> comparator) {
        this.iterator = iterator;
        this.comparator = comparator;
    }

    @Override
    protected void nextIteration() {
        if (!isInit) {
            final List<T> list = Operators.<T>toList(iterator);
            Collections.sort(list, comparator);
            sortedIterator = list.iterator();
        }
        hasNext = sortedIterator.hasNext();
        if (hasNext) {
            next = sortedIterator.next();
        }
    }
}
