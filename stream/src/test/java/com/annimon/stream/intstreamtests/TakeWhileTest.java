package com.annimon.stream.intstreamtests;

import com.annimon.stream.Functions;
import com.annimon.stream.IntStream;
import org.junit.Test;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

public final class TakeWhileTest {

    @Test
    public void testTakeWhile() {
        int[] expected = {2, 4, 6};
        int[] actual = IntStream.of(2, 4, 6, 7, 8, 10, 11)
                .takeWhile(Functions.remainderInt(2))
                .toArray();
        assertThat(actual, is(expected));
    }

    @Test
    public void testTakeWhileNonFirstMatch() {
        assertThat(
                IntStream.of(2, 4, 6, 7, 8, 10, 11)
                        .takeWhile(Functions.remainderInt(3))
                         .count(),
                is(0L));
    }

    @Test
    public void testTakeWhileAllMatch() {
        long count = IntStream.of(2, 4, 6, 7, 8, 10, 11)
                .takeWhile(Functions.remainderInt(1))
                .count();
        assertEquals(7, count);
    }
}
