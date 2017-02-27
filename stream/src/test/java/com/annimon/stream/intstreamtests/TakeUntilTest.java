package com.annimon.stream.intstreamtests;

import com.annimon.stream.Functions;
import com.annimon.stream.IntStream;
import com.annimon.stream.function.IntPredicate;
import org.junit.Test;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

public final class TakeUntilTest {

    @Test
    public void testTakeUntil() {
        int[] expected = {2, 4, 6, 7};
        int[] actual = IntStream.of(2, 4, 6, 7, 8, 10, 11)
                .takeUntil(IntPredicate.Util.negate(Functions.remainderInt(2)))
                .toArray();
        assertThat(actual, is(expected));
    }

    @Test
    public void testTakeUntilFirstMatch() {
        int[] expected = {2};
        int[] actual = IntStream.of(2, 4, 6, 7, 8, 10, 11)
                .takeUntil(Functions.remainderInt(2))
                .toArray();
        assertThat(actual, is(expected));
    }

    @Test
    public void testTakeUntilNoneMatch() {
        long count = IntStream.of(2, 4, 6, 7, 8, 10, 11)
                .takeUntil(Functions.remainderInt(128))
                .count();
        assertEquals(7, count);
    }
}
