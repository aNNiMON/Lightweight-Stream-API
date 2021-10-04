package com.annimon.stream.intstreamtests;

import static org.junit.Assert.assertEquals;

import com.annimon.stream.IntStream;
import org.junit.Test;

public final class FindFirstOrElseTest {

    @Test
    public void testFindFirstOrElse() {
        assertEquals(42, IntStream.of(42).findFirstOrElse(10));
        assertEquals(10, IntStream.empty().findFirstOrElse(10));
    }
}
