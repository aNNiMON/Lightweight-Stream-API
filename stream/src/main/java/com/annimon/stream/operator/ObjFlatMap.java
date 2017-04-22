package com.annimon.stream.operator;

import com.annimon.stream.Stream;
import com.annimon.stream.function.Function;
import com.annimon.stream.iterator.LsaExtIterator;
import java.util.Iterator;

public class ObjFlatMap<T, R> extends LsaExtIterator<R> {

    private final Iterator<? extends T> iterator;
    private final Function<? super T, ? extends Stream<? extends R>> mapper;
    private Iterator<? extends R> inner;
    private Stream<? extends R> innerStream;

    public ObjFlatMap(Iterator<? extends T> iterator,
            Function<? super T, ? extends Stream<? extends R>> mapper) {
        this.iterator = iterator;
        this.mapper = mapper;
    }

    @Override
    protected void nextIteration() {
        if ((inner != null) && inner.hasNext()) {
            next = inner.next();
            hasNext = true;
            return;
        }
        while (iterator.hasNext()) {
            if (inner == null || !inner.hasNext()) {
                if (innerStream != null) {
                    innerStream.close();
                    innerStream = null;
                }
                final T arg = iterator.next();
                final Stream <? extends R> result = mapper.apply(arg);
                if (result != null) {
                    inner = result.iterator();
                    innerStream = result;
                }
            }
            if ((inner != null) && inner.hasNext()) {
                next = inner.next();
                hasNext = true;
                return;
            }
        }
        hasNext = false;
        if (innerStream != null) {
            innerStream.close();
            innerStream = null;
        }
    }
}
