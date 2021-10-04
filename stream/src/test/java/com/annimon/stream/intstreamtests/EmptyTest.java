package com.annimon.stream.intstreamtests;

import static org.junit.Assert.assertEquals;

import com.annimon.stream.IntStream;
import org.junit.Test;

public final class EmptyTest {

    @Test
    public void testStreamEmpty() {
        assertEquals(0, IntStream.empty().count());
        assertEquals(0, IntStream.empty().iterator().nextInt());
    }
}
