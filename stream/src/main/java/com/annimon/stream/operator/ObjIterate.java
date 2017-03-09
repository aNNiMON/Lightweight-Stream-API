package com.annimon.stream.operator;

import com.annimon.stream.function.UnaryOperator;
import com.annimon.stream.iterator.LsaIterator;

public class ObjIterate<T> extends LsaIterator<T> {

    private final UnaryOperator<T> op;
    private T current;

    public ObjIterate(T seed, UnaryOperator<T> op) {
        this.op = op;
        current = seed;
    }

    @Override
    public boolean hasNext() {
        return true;
    }

    @Override
    public T nextIteration() {
        final T old = current;
        current = op.apply(current);
        return old;
    }
}
