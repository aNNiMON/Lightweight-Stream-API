package com.annimon.stream.operator;

import com.annimon.stream.function.IndexedFunction;
import com.annimon.stream.iterator.IndexedIterator;
import com.annimon.stream.iterator.LsaIterator;

public class ObjMapIndexed<T, R> extends LsaIterator<R> {

    private final IndexedIterator<? extends T> iterator;
    private final IndexedFunction<? super T, ? extends R> mapper;

    public ObjMapIndexed(IndexedIterator<? extends T> iterator, IndexedFunction<? super T, ? extends R> mapper) {
        this.iterator = iterator;
        this.mapper = mapper;
    }

    @Override
    public boolean hasNext() {
        return iterator.hasNext();
    }

    @Override
    public R nextIteration() {
        return mapper.apply(iterator.getIndex(), iterator.next());
    }
}
