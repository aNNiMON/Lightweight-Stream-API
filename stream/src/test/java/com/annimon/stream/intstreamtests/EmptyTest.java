package com.annimon.stream.intstreamtests;

import com.annimon.stream.IntStream;
import org.junit.Test;
import static org.junit.Assert.assertTrue;

public final class EmptyTest {

    @Test
    public void testStreamEmpty() {
        assertTrue(IntStream.empty().count() == 0);
        assertTrue(IntStream.empty().iterator().nextInt() == 0);
    }
}
