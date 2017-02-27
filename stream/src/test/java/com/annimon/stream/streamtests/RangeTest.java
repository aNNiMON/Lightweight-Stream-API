package com.annimon.stream.streamtests;

import com.annimon.stream.Stream;
import java.util.Arrays;
import org.junit.Test;
import static com.annimon.stream.test.hamcrest.StreamMatcher.assertElements;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertEquals;

public final class RangeTest {

    @Test
    public void testStreamRange() {
        Stream.range(0, 5)
                .custom(assertElements(is(Arrays.asList(
                      0, 1, 2, 3, 4
                ))));
    }

    @Test
    public void testStreamRangeOnMaxValues() {
        long count = Stream.range(Integer.MAX_VALUE - 10, Integer.MAX_VALUE).count();
        assertEquals(10L, count);
    }

    @Test
    public void testStreamRangeOnMaxLongValues() {
        long count = Stream.range(Long.MAX_VALUE - 10, Long.MAX_VALUE).count();
        assertEquals(10L, count);
    }

    @Test
    public void testStreamRangeClosed() {
        Stream.rangeClosed(0, 5)
                .custom(assertElements(is(Arrays.asList(
                      0, 1, 2, 3, 4, 5
                ))));
    }

    @Test
    public void testStreamRangeClosedOnMaxValues() {
        long count = Stream.rangeClosed(Integer.MAX_VALUE - 10, Integer.MAX_VALUE).count();
        assertEquals(11L, count);
    }

    @Test
    public void testStreamRangeClosedOnMaxLongValues() {
        long count = Stream.rangeClosed(Long.MAX_VALUE - 10, Long.MAX_VALUE).count();
        assertEquals(11L, count);
    }

    @Test
    @SuppressWarnings("deprecation")
    public void testStreamOfRange() {
        long count = Stream.ofRange(0, 5).count();
        assertEquals(5, count);
    }

    @Test
    @SuppressWarnings("deprecation")
    public void testStreamOfRangeLong() {
        long count = Stream.ofRange(Long.MAX_VALUE - 10, Long.MAX_VALUE).count();
        assertEquals(10L, count);
    }

    @Test
    @SuppressWarnings("deprecation")
    public void testStreamOfRangeClosed() {
        long count = Stream.ofRangeClosed(0, 5).count();
        assertEquals(6, count);
    }

    @Test
    @SuppressWarnings("deprecation")
    public void testStreamOfRangeClosedLong() {
        long count = Stream.ofRangeClosed(Long.MAX_VALUE - 10, Long.MAX_VALUE).count();
        assertEquals(11L, count);
    }
}
