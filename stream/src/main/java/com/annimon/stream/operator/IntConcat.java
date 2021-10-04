package com.annimon.stream.operator;

import com.annimon.stream.iterator.PrimitiveExtIterator;
import com.annimon.stream.iterator.PrimitiveIterator;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.jetbrains.annotations.NotNull;

public class IntConcat extends PrimitiveExtIterator.OfInt {

    private final List<? extends PrimitiveIterator.OfInt> iterators;
    private final int iteratorsCount;
    private int iteratorIndex;

    public IntConcat(
            @NotNull PrimitiveIterator.OfInt iterator1,
            @NotNull PrimitiveIterator.OfInt iterator2) {
        iterators = Arrays.asList(iterator1, iterator2);
        iteratorsCount = 2;
        iteratorIndex = 0;
    }

    public IntConcat(@NotNull List<? extends PrimitiveIterator.OfInt> iterators) {
        this.iterators = new ArrayList<PrimitiveIterator.OfInt>(iterators);
        iteratorsCount = iterators.size();
        iteratorIndex = 0;
    }

    @Override
    protected void nextIteration() {
        while (iteratorIndex < iteratorsCount) {
            PrimitiveIterator.OfInt currentIterator = iterators.get(iteratorIndex);
            if (currentIterator.hasNext()) {
                next = currentIterator.nextInt();
                hasNext = true;
                return;
            }
            iteratorIndex++;
        }
        hasNext = false;
    }
}
