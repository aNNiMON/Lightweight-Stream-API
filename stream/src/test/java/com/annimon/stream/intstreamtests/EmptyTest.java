package com.annimon.stream.intstreamtests;

import com.annimon.stream.IntStream;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public final class EmptyTest {

    @Test
    public void testStreamEmpty() {
        assertEquals(0, IntStream.empty().count());
        assertEquals(0, IntStream.empty().iterator().nextInt());
    }
}
