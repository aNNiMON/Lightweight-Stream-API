package com.annimon.stream.intstreamtests;

import com.annimon.stream.Functions;
import com.annimon.stream.IntStream;
import org.junit.Test;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

public final class DropWhileTest {

    @Test
    public void testDropWhile() {
        int[] expected = {7, 8, 10, 11};
        int[] actual  = IntStream.of(2, 4, 6, 7, 8, 10, 11)
                .dropWhile(Functions.remainderInt(2))
                .toArray();
        assertThat(actual, is(expected));
    }

    @Test
    public void testDropWhileNonFirstMatch() {
        long count = IntStream.of(2, 4, 6, 7, 8, 10, 11)
                .dropWhile(Functions.remainderInt(3))
                .count();
        assertEquals(7, count);
    }

    @Test
    public void testDropWhileAllMatch() {
        assertThat(
                IntStream.of(2, 4, 6, 7, 8, 10, 11)
                        .dropWhile(Functions.remainderInt(1))
                        .count(),
                is(0L));
    }
}
