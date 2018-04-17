package com.annimon.stream.intstreamtests;

import com.annimon.stream.Functions;
import com.annimon.stream.IntStream;
import com.annimon.stream.Stream;
import com.annimon.stream.function.IntConsumer;
import com.annimon.stream.function.IntUnaryOperator;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.junit.Test;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

public final class SortedTest {

    @Test
    public void testSorted() {
        assertEquals(0, IntStream.empty().sorted().count());
        assertEquals(42, IntStream.of(42).sorted().findFirst().getAsInt());

        final boolean[] wrongOrder = new boolean[]{false};

        IntStream.iterate(2, new IntUnaryOperator() {
            @Override
            public int applyAsInt(int operand) {
                return -operand + 1;
            }
        })
        .limit(1000)
        .sorted()
        .forEach(new IntConsumer() {

            int currentValue = Integer.MIN_VALUE;

            @Override
            public void accept(int value) {
                if(value < currentValue) {
                    wrongOrder[0] = true;
                }
                currentValue = value;
            }
        });

        assertTrue(!wrongOrder[0]);
    }

    @Test
    public void testSortedLazy() {
        int[] expected = { -7, 0, 3, 6, 9, 19 };

        List<Integer> input = new ArrayList<Integer>(6);
        input.addAll(Arrays.asList(6, 3, 9));
        IntStream stream = Stream.of(input).mapToInt(Functions.toInt()).sorted();
        input.addAll(Arrays.asList(0, -7, 19));

        assertThat(stream.toArray(), is(expected));
    }

    @Test
    public void testSortedWithComparator() {
        int[] expected = { 19, 9, -7, 6, 3, 0 };

        int[] actual = IntStream.of(6, 3, 9, 0, -7, 19)
                .sorted(Functions.descendingAbsoluteOrder())
                .toArray();
        assertThat(actual, is(expected));
    }
}
