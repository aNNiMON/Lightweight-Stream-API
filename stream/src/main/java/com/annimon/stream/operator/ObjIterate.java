package com.annimon.stream.operator;

import com.annimon.stream.function.UnaryOperator;
import com.annimon.stream.iterator.LsaIterator;
import jdk.internal.jline.internal.Nullable;
import org.jetbrains.annotations.NotNull;

public class ObjIterate<T> extends LsaIterator<T> {

    private final UnaryOperator<T> op;
    private T current;

    public ObjIterate(@Nullable T seed, @NotNull UnaryOperator<T> op) {
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
