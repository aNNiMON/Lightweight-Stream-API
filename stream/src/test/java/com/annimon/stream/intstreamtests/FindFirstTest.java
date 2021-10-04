package com.annimon.stream.intstreamtests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.annimon.stream.IntStream;
import org.junit.Test;

public final class FindFirstTest {

    @Test
    public void testFindFirst() {
        assertFalse(IntStream.empty().findFirst().isPresent());
        assertEquals(IntStream.of(42).findFirst().getAsInt(), 42);
        assertTrue(IntStream.rangeClosed(2, 5).findFirst().isPresent());
    }
}
