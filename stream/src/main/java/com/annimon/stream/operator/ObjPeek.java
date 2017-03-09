package com.annimon.stream.operator;

import com.annimon.stream.function.Consumer;
import com.annimon.stream.iterator.LsaIterator;
import java.util.Iterator;

public class ObjPeek<T> extends LsaIterator<T> {

    private final Iterator<? extends T> iterator;
    private final Consumer<? super T> action;

    public ObjPeek(Iterator<? extends T> iterator, Consumer<? super T> action) {
        this.iterator = iterator;
        this.action = action;
    }

    @Override
    public boolean hasNext() {
        return iterator.hasNext();
    }

    @Override
    public T nextIteration() {
        final T value = iterator.next();
        action.accept(value);
        return value;
    }
}
