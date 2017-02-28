package com.annimon.stream.longstreamtests;

import com.annimon.stream.Functions;
import com.annimon.stream.LongStream;
import org.junit.Test;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public final class AnyMatchTest {

    @Test
    public void testAnyMatch() {
        assertTrue(LongStream.of(3, 10, 19, 4, 50)
                .anyMatch(Functions.remainderLong(2)));

        assertTrue(LongStream.of(10, 4, 50)
                .anyMatch(Functions.remainderLong(2)));

        assertFalse(LongStream.of(3, 19)
                .anyMatch(Functions.remainderLong(2)));

        assertFalse(LongStream.empty()
                .anyMatch(Functions.remainderLong(2)));
    }
}
