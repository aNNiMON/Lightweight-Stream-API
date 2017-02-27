package com.annimon.stream.streamtests;

import com.annimon.stream.Stream;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public final class CountTest {

    @Test
    public void testCount() {
        long count = Stream.range(10000000000L, 10000002000L).count();
        assertEquals(2000, count);
    }

    @Test
    public void testCountMinValue() {
        long count = Stream.range(Integer.MIN_VALUE, Integer.MIN_VALUE + 100).count();
        assertEquals(100, count);
    }

    @Test
    public void testCountMaxValue() {
        long count = Stream.range(Long.MAX_VALUE - 100, Long.MAX_VALUE).count();
        assertEquals(100, count);
    }
}
