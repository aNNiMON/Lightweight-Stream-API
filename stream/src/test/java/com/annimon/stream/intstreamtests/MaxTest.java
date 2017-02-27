package com.annimon.stream.intstreamtests;

import com.annimon.stream.IntStream;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public final class MaxTest {

    @Test
    public void testMax() {
        assertFalse(IntStream.empty().max().isPresent());

        assertTrue(IntStream.of(42).max().isPresent());
        assertEquals(IntStream.of(42).max().getAsInt(), 42);

        assertEquals(IntStream.of(-1, -2, -3, -2, -3, -5, -2, Integer.MIN_VALUE, Integer.MAX_VALUE)
                .max().getAsInt(), Integer.MAX_VALUE);
    }
}
