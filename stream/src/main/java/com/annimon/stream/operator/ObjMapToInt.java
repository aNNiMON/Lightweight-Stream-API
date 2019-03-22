package com.annimon.stream.operator;

import com.annimon.stream.function.ToIntFunction;
import com.annimon.stream.iterator.PrimitiveIterator;
import java.util.Iterator;
import org.jetbrains.annotations.NotNull;

public class ObjMapToInt<T> extends PrimitiveIterator.OfInt {

    private final Iterator<? extends T> iterator;
    private final ToIntFunction<? super T> mapper;

    public ObjMapToInt(
            @NotNull Iterator<? extends T> iterator,
            @NotNull ToIntFunction<? super T> mapper) {
        this.iterator = iterator;
        this.mapper = mapper;
    }

    @Override
    public boolean hasNext() {
        return iterator.hasNext();
    }

    @Override
    public int nextInt() {
        return mapper.applyAsInt(iterator.next());
    }
}
