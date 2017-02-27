package com.annimon.stream.intstreamtests;

import com.annimon.stream.IntStream;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public final class SumTest {

    @Test
    public void testSum() {
        assertEquals(IntStream.empty().sum(), 0);
        assertEquals(IntStream.of(42).sum(), 42);
        assertEquals(IntStream.rangeClosed(4, 8).sum(), 30);
    }
}
