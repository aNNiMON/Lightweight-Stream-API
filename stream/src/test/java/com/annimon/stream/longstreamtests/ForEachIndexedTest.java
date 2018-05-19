package com.annimon.stream.longstreamtests;

import com.annimon.stream.IntPair;
import com.annimon.stream.LongStream;
import com.annimon.stream.function.IndexedLongConsumer;
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
        final List<IntPair<Long>> result = new ArrayList<IntPair<Long>>();
        LongStream.of(1000, 2000, 3000)
                .forEachIndexed(new IndexedLongConsumer() {
                    @Override
                    public void accept(int index, long value) {
                        result.add(new IntPair<Long>(index, value));
                    }
                });
        assertThat(result, is(Arrays.asList(
                new IntPair<Long>(0, 1000L),
                new IntPair<Long>(1, 2000L),
                new IntPair<Long>(2, 3000L)
        )));
    }

    @Test
    @SuppressWarnings("unchecked")
    public void testForEachIndexedWithStartAndStep() {
        final List<IntPair<Long>> result = new ArrayList<IntPair<Long>>();
        LongStream.of(1000, 2000, 3000)
                .forEachIndexed(50, -10, new IndexedLongConsumer() {
                    @Override
                    public void accept(int index, long value) {
                        result.add(new IntPair<Long>(index, value));
                    }
                });
        assertThat(result, is(Arrays.asList(
                new IntPair<Long>(50, 1000L),
                new IntPair<Long>(40, 2000L),
                new IntPair<Long>(30, 3000L)
        )));
    }
}
