package com.annimon.stream.longstreamtests;

import com.annimon.stream.LongStream;
import org.junit.Test;
import static com.annimon.stream.test.hamcrest.LongStreamMatcher.elements;
import static com.annimon.stream.test.hamcrest.LongStreamMatcher.isEmpty;
import static org.hamcrest.Matchers.arrayContaining;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

public final class RangeTest {

    @Test
    public void testStreamRange() {
        assertEquals(10, LongStream.range(1, 5).sum());
        assertEquals(0, LongStream.range(2, 2).count());
    }

    @Test(timeout = 1000)
    public void testStreamRangeOnMinValue() {
        assertThat(LongStream.range(Long.MIN_VALUE, Long.MIN_VALUE + 5).count(), is(5L));
    }

    @Test(timeout = 1000)
    public void testStreamRangeOnEqualValues() {
        assertThat(LongStream.range(Long.MIN_VALUE, Long.MIN_VALUE), isEmpty());

        assertThat(LongStream.range(0, 0), isEmpty());

        assertThat(LongStream.range(Long.MAX_VALUE, Long.MAX_VALUE), isEmpty());
    }

    @Test(timeout = 1000)
    public void testStreamRangeOnMaxValue() {
        assertThat(LongStream.range(Long.MAX_VALUE - 5, Long.MAX_VALUE).count(), is(5L));
    }

    @Test
    public void testStreamRangeClosed() {
        assertThat(LongStream.rangeClosed(1, 5).sum(), is(15L));
        assertThat(LongStream.rangeClosed(1, 5).count(), is(5L));
    }

    @Test
    public void testStreamRangeClosedStartGreaterThanEnd() {
        assertThat(LongStream.rangeClosed(5, 1), isEmpty());
    }

    @Test(timeout = 1000)
    public void testStreamRangeClosedOnMinValue() {
        assertThat(LongStream.rangeClosed(Long.MIN_VALUE, Long.MIN_VALUE + 5).count(), is(6L));
    }

    @Test(timeout = 1000)
    public void testStreamRangeClosedOnEqualValues() {
        assertThat(LongStream.rangeClosed(Long.MIN_VALUE, Long.MIN_VALUE),
                elements(arrayContaining(Long.MIN_VALUE)));

        assertThat(LongStream.rangeClosed(0, 0),
                elements(arrayContaining(0L)));

        assertThat(LongStream.rangeClosed(Long.MAX_VALUE, Long.MAX_VALUE),
                elements(arrayContaining(Long.MAX_VALUE)));
    }

    @Test(timeout = 1000)
    public void testStreamRangeClosedOnMaxValue() {
        assertThat(LongStream.rangeClosed(Long.MAX_VALUE - 5, Long.MAX_VALUE).count(), is(6L));
    }
}
