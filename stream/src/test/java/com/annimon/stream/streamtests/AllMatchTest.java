package com.annimon.stream.streamtests;

import com.annimon.stream.Functions;
import com.annimon.stream.Stream;
import org.junit.Test;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public final class AllMatchTest {

    @Test
    public void testAllMatchWithFalseResult() {
        boolean match = Stream.range(0, 10)
                .allMatch(Functions.remainder(2));
        assertFalse(match);
    }

    @Test
    public void testAllMatchWithTrueResult() {
        boolean match = Stream.of(2, 4, 6, 8, 10)
                .allMatch(Functions.remainder(2));
        assertTrue(match);
    }
}
