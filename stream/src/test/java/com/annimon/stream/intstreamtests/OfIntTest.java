package com.annimon.stream.intstreamtests;

import com.annimon.stream.IntStream;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public final class OfIntTest {

    @Test
    public void testStreamOfInt() {
        assertEquals(1, IntStream.of(42).count());
        assertTrue(IntStream.of(42).findFirst().isPresent());
    }
}
