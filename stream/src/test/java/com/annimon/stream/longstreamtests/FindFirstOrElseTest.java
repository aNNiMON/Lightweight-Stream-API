package com.annimon.stream.longstreamtests;

import static org.junit.Assert.assertEquals;

import com.annimon.stream.LongStream;
import org.junit.Test;

public final class FindFirstOrElseTest {

    @Test
    public void testFindFirstOrElse() {
        assertEquals(42, LongStream.of(42).findFirstOrElse(10));
        assertEquals(10, LongStream.empty().findFirstOrElse(10));
    }
}
