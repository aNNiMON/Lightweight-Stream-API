package com.annimon.stream.intstreamtests;

import com.annimon.stream.IntStream;
import org.junit.Test;
import static org.junit.Assert.assertTrue;

public final class OfIntTest {

    @Test
    public void testStreamOfInt() {
        assertTrue(IntStream.of(42).count() == 1);
        assertTrue(IntStream.of(42).findFirst().isPresent());
    }
}
