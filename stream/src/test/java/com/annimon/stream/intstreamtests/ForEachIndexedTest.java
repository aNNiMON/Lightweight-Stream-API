package com.annimon.stream.intstreamtests;

import com.annimon.stream.IntPair;
import com.annimon.stream.IntStream;
import com.annimon.stream.function.IndexedIntConsumer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.junit.Test;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public final class ForEachIndexedTest {

    @Test
    @SuppressWarnings("unchecked")
    public void testForEachIndexed() {
        final List<IntPair<Integer>> result = new ArrayList<IntPair<Integer>>();
        IntStream.of(1000, 2000, 3000)
                .forEachIndexed(new IndexedIntConsumer() {
                    @Override
                    public void accept(int index, int value) {
                        result.add(new IntPair<Integer>(index, value));
                    }
                });
        assertThat(result, is(Arrays.asList(
                new IntPair<Integer>(0, 1000),
                new IntPair<Integer>(1, 2000),
                new IntPair<Integer>(2, 3000)
        )));
    }

    @Test
    @SuppressWarnings("unchecked")
    public void testForEachIndexedWithStartAndStep() {
        final List<IntPair<Integer>> result = new ArrayList<IntPair<Integer>>();
        IntStream.of(1000, 2000, 3000)
                .forEachIndexed(50, -10, new IndexedIntConsumer() {
                    @Override
                    public void accept(int index, int value) {
                        result.add(new IntPair<Integer>(index, value));
                    }
                });
        assertThat(result, is(Arrays.asList(
                new IntPair<Integer>(50, 1000),
                new IntPair<Integer>(40, 2000),
                new IntPair<Integer>(30, 3000)
        )));
    }
}
