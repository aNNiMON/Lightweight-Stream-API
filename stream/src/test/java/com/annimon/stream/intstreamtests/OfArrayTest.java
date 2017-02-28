package com.annimon.stream.intstreamtests;

import com.annimon.stream.IntStream;
import org.junit.Test;
import static com.annimon.stream.test.hamcrest.IntStreamMatcher.assertIsEmpty;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public final class OfArrayTest {

    @Test
    public void testStreamOfInts() {
        int[] data1 = {1, 2, 3, 4, 5};
        int[] data2 = {42};
        int[] data3 = {};

        assertTrue(IntStream.of(data1).count() == 5);
        assertTrue(IntStream.of(data2).findFirst().getAsInt() == 42);
        assertFalse(IntStream.of(data3).findFirst().isPresent());
    }

    @Test(expected = NullPointerException.class)
    public void testStreamOfIntsNull() {
        IntStream.of((int[]) null);
    }

    @Test
    public void testStreamOfEmptyArray() {
        IntStream.of(new int[0])
                .custom(assertIsEmpty());
    }
}
