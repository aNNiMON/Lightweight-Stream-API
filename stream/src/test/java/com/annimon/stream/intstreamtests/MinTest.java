package com.annimon.stream.intstreamtests;

import com.annimon.stream.IntStream;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public final class MinTest {

    @Test
    public void testMin() {
        assertFalse(IntStream.empty().min().isPresent());

        assertTrue(IntStream.of(42).min().isPresent());
        assertEquals(IntStream.of(42).min().getAsInt(), 42);

        assertEquals(IntStream.of(-1, -2, -3, -2, -3, -5, -2, Integer.MIN_VALUE, Integer.MAX_VALUE)
                .min().getAsInt(), Integer.MIN_VALUE);
    }
}
