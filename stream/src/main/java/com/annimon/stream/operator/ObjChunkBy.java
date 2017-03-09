package com.annimon.stream.operator;

import com.annimon.stream.function.Function;
import com.annimon.stream.iterator.LsaIterator;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ObjChunkBy<T, K> extends LsaIterator<List<T>> {

    private final Iterator<? extends T> iterator;
    private final Function<? super T, ? extends K> classifier;
    private T next;
    private boolean peekedNext;

    public ObjChunkBy(Iterator<? extends T> iterator, Function<? super T, ? extends K> classifier) {
        this.iterator = iterator;
        this.classifier = classifier;
    }

    @Override
    public boolean hasNext() {
        return peekedNext || iterator.hasNext();
    }

    @Override
    public List<T> nextIteration() {
        final K key = classifier.apply(peek());

        final List<T> list = new ArrayList<T>();
        do {
            list.add(takeNext());
        } while ( iterator.hasNext() && key.equals(classifier.apply(peek())) );

        return list;
    }

    private T takeNext() {
        final T element = peek();
        peekedNext = false;
        return element;
    }

    private T peek() {
        if (!peekedNext) {
            next = iterator.next();
            peekedNext = true;
        }
        return next;
    }
}
