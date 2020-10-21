package com.annimon.stream.operator;

import com.annimon.stream.iterator.PrimitiveExtIterator;
import com.annimon.stream.iterator.PrimitiveIterator;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.jetbrains.annotations.NotNull;

public class DoubleConcat extends PrimitiveExtIterator.OfDouble {

    private final List<? extends PrimitiveIterator.OfDouble> iterators;
    private final int iteratorsCount;
    private int iteratorIndex;

    public DoubleConcat(
            @NotNull PrimitiveIterator.OfDouble iterator1,
            @NotNull PrimitiveIterator.OfDouble iterator2) {
        iterators = Arrays.asList(iterator1, iterator2);
        iteratorsCount = 2;
        iteratorIndex = 0;
    }

    public DoubleConcat(@NotNull List<? extends PrimitiveIterator.OfDouble> iterators) {
        this.iterators = new ArrayList<PrimitiveIterator.OfDouble>(iterators);
        iteratorsCount = iterators.size();
        iteratorIndex = 0;
    }


    @Override
    protected void nextIteration() {
        while (iteratorIndex < iteratorsCount) {
            PrimitiveIterator.OfDouble currentIterator = iterators.get(iteratorIndex);
            if (currentIterator.hasNext()) {
                next = currentIterator.nextDouble();
                hasNext = true;
                return;
            }
            iteratorIndex++;
        }
        hasNext = false;
    }
}
