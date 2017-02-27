package com.annimon.stream.intstreamtests;

import com.annimon.stream.Functions;
import com.annimon.stream.IntStream;
import org.junit.Test;
import static org.junit.Assert.assertTrue;

public final class FilterNotTest {

    @Test
    public void testFilterNot() {
        assertTrue(IntStream.rangeClosed(1, 10).filterNot(Functions.remainderInt(2)).count() == 5);

        assertTrue(IntStream.rangeClosed(1, 10).filterNot(Functions.remainderInt(2)).sum() == 25);
    }
}
