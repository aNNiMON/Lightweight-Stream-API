package com.annimon.stream.operator;

import com.annimon.stream.function.BiFunction;
import com.annimon.stream.iterator.LsaIterator;
import java.util.Iterator;
import org.jetbrains.annotations.NotNull;

public class ObjZip<F, S, R> extends LsaIterator<R> {

    private final Iterator<? extends F> iterator1;
    private final Iterator<? extends S> iterator2;
    private final BiFunction<? super F, ? super S, ? extends R> combiner;

    public ObjZip(
            @NotNull Iterator<? extends F> iterator1,
            @NotNull Iterator<? extends S> iterator2,
            @NotNull BiFunction<? super F, ? super S, ? extends R> combiner) {
        this.iterator1 = iterator1;
        this.iterator2 = iterator2;
        this.combiner = combiner;
    }

    @Override
    public boolean hasNext() {
        return iterator1.hasNext() && iterator2.hasNext();
    }

    @Override
    public R nextIteration() {
        return combiner.apply(iterator1.next(), iterator2.next());
    }
}
