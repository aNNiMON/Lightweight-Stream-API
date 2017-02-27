package com.annimon.stream.longstreamtests;

import com.annimon.stream.Functions;
import com.annimon.stream.LongStream;
import org.junit.Test;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public final class AllMatchTest {

    @Test
    public void testAllMatch() {
        assertFalse(LongStream.of(3, 10, 19, 4, 50)
                .allMatch(Functions.remainderLong(2)));

        assertTrue(LongStream.of(10, 4, 50)
                .allMatch(Functions.remainderLong(2)));

        assertFalse(LongStream.of(3, 19)
                .allMatch(Functions.remainderLong(2)));

        assertTrue(LongStream.empty()
                .allMatch(Functions.remainderLong(2)));
    }
}
