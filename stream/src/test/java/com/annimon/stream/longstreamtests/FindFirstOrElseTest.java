package com.annimon.stream.longstreamtests;

import com.annimon.stream.LongStream;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public final class FindFirstOrElseTest {

    @Test
    public void testFindFirstOrElse() {
        assertEquals(42, LongStream.of(42).findFirstOrElse(10));
        assertEquals(10, LongStream.empty().findFirstOrElse(10));
    }
}
