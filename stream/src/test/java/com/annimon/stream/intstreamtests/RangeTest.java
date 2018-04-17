package com.annimon.stream.intstreamtests;

import com.annimon.stream.IntStream;
import org.junit.Test;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

public final class RangeTest {

    @Test
    public void testStreamRange() {
        assertEquals(10, IntStream.range(1, 5).sum());
        assertEquals(0, IntStream.range(2, 2).count());
    }

    @Test
    public void testStreamRangeOnMinValue() {
        assertThat(IntStream.range(Integer.MIN_VALUE, Integer.MIN_VALUE + 5).count(), is(5L));
    }

    @Test
    public void testStreamRangeOnEqualValues() {
        assertThat(IntStream.range(Integer.MIN_VALUE, Integer.MIN_VALUE).count(), is(0L));

        assertThat(IntStream.range(0, 0).count(), is(0L));

        assertThat(IntStream.range(Integer.MAX_VALUE, Integer.MAX_VALUE).count(), is(0L));
    }

    @Test
    public void testStreamRangeOnMaxValue() {
        assertThat(IntStream.range(Integer.MAX_VALUE - 5, Integer.MAX_VALUE).count(), is(5L));
    }

    @Test
    public void testStreamRangeClosed() {
        assertEquals(15, IntStream.rangeClosed(1, 5).sum());
        assertEquals(5, IntStream.rangeClosed(1, 5).count());
    }

    @Test
    public void testStreamRangeClosedStartGreaterThanEnd() {
        assertThat(IntStream.rangeClosed(5, 1).count(), is(0L));
    }

    @Test
    public void testStreamRangeClosedOnMinValue() {
        assertThat(IntStream.rangeClosed(Integer.MIN_VALUE, Integer.MIN_VALUE + 5).count(), is(6L));
    }

    @Test
    public void testStreamRangeClosedOnEqualValues() {
        assertThat(IntStream.rangeClosed(Integer.MIN_VALUE, Integer.MIN_VALUE).single(), is(Integer.MIN_VALUE));

        assertThat(IntStream.rangeClosed(0, 0).single(), is(0));

        assertThat(IntStream.rangeClosed(Integer.MAX_VALUE, Integer.MAX_VALUE).single(), is(Integer.MAX_VALUE));
    }

    @Test
    public void testStreamRangeClosedOnMaxValue() {
        assertThat(IntStream.rangeClosed(Integer.MAX_VALUE - 5, Integer.MAX_VALUE).count(), is(6L));
    }
}
