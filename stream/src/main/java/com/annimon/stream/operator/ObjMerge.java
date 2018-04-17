package com.annimon.stream.operator;

import com.annimon.stream.function.BiFunction;
import com.annimon.stream.iterator.LsaIterator;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;

public class ObjMerge<T> extends LsaIterator<T> {

    public enum MergeResult {
        TAKE_FIRST, TAKE_SECOND
    }

    private final Iterator<? extends T> iterator1;
    private final Iterator<? extends T> iterator2;
    private final BiFunction<? super T, ? super T, MergeResult> selector;
    private final Queue<T> buffer1;
    private final Queue<T> buffer2;

    public ObjMerge(Iterator<? extends T> iterator1, Iterator<? extends T> iterator2,
                    BiFunction<? super T, ? super T, MergeResult> selector) {
        this.iterator1 = iterator1;
        this.iterator2 = iterator2;
        this.selector = selector;
        buffer1 = new LinkedList<T>();
        buffer2 = new LinkedList<T>();
    }

    @Override
    public boolean hasNext() {
        return !buffer1.isEmpty() || !buffer2.isEmpty()
                || iterator1.hasNext() || iterator2.hasNext();
    }

    @Override
    public T nextIteration() {
        if (!buffer1.isEmpty()) {
            final T v1 = buffer1.poll();
            if (iterator2.hasNext()) {
                return select(v1, iterator2.next());
            }
            return v1;
        }
        if (!buffer2.isEmpty()) {
            final T v2 = buffer2.poll();
            if (iterator1.hasNext()) {
                return select(iterator1.next(), v2);
            }
            return v2;
        }

        if (!iterator1.hasNext()) {
            return iterator2.next();
        }
        if (!iterator2.hasNext()) {
            return iterator1.next();
        }

        return select(iterator1.next(), iterator2.next());
    }

    private T select(T v1, T v2) {
        final MergeResult result = selector.apply(v1, v2);
        switch (result) {
            case TAKE_FIRST:
                buffer2.add(v2);
                return v1;

            case TAKE_SECOND:
            default:
                buffer1.add(v1);
                return v2;
        }
    }
}
