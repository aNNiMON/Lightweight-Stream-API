package com.annimon.stream.operator;

import com.annimon.stream.function.Function;
import com.annimon.stream.iterator.LsaExtIterator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class ObjDistinctBy<T, K> extends LsaExtIterator<T> {

    private final Iterator<? extends T> iterator;
    private final Function<? super T, ? extends K> classifier;
    private final Set<K> set;

    public ObjDistinctBy(Iterator<? extends T> iterator, Function<? super T, ? extends K> classifier) {
        this.iterator = iterator;
        this.classifier = classifier;
        set = new HashSet<K>();
    }

    @Override
    protected void nextIteration() {
        while (hasNext = iterator.hasNext()) {
            next = iterator.next();
            final K key = classifier.apply(next);
            if (set.add(key)) {
                return;
            }
        }
    }
}
