package com.annimon.stream.streamtests;

import com.annimon.stream.Functions;
import com.annimon.stream.Stream;
import org.junit.Test;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public final class NoneMatchTest {

    @Test
    public void testNoneMatchWithFalseResult() {
        boolean match = Stream.range(0, 10)
                .noneMatch(Functions.remainder(2));
        assertFalse(match);
    }

    @Test
    public void testNoneMatchWithTrueResult() {
        boolean match = Stream.of(2, 3, 5, 8, 13)
                .noneMatch(Functions.remainder(10));
        assertTrue(match);
    }
}
