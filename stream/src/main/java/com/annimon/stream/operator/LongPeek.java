package com.annimon.stream.operator;

import com.annimon.stream.function.LongConsumer;
import com.annimon.stream.iterator.PrimitiveIterator;

public class LongPeek extends PrimitiveIterator.OfLong {

    private final PrimitiveIterator.OfLong iterator;
    private final LongConsumer action;

    public LongPeek(PrimitiveIterator.OfLong iterator, LongConsumer action) {
        this.iterator = iterator;
        this.action = action;
    }

    @Override
    public boolean hasNext() {
        return iterator.hasNext();
    }

    @Override
    public long nextLong() {
        final long value = iterator.nextLong();
        action.accept(value);
        return value;
    }
}
