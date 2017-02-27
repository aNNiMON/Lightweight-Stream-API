package com.annimon.stream.longstreamtests;

import com.annimon.stream.Functions;
import com.annimon.stream.LongStream;
import com.annimon.stream.function.LongPredicate;
import org.junit.Test;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public final class TakeUntilTest {

    @Test
    public void testTakeUntil() {
        long[] expected = {2, 4, 6, 7};
        long[] actual = LongStream.of(2, 4, 6, 7, 8, 10, 11)
                .takeUntil(LongPredicate.Util.negate(Functions.remainderLong(2)))
                .toArray();
        assertThat(actual, is(expected));
    }

    @Test
    public void testTakeUntilFirstMatch() {
        long[] expected = {2};
        long[] actual = LongStream.of(2, 4, 6, 7, 8, 10, 11)
                .takeUntil(Functions.remainderLong(2))
                .toArray();
        assertThat(actual, is(expected));
    }

    @Test
    public void testTakeUntilNoneMatch() {
        long count = LongStream.of(2, 4, 6, 7, 8, 10, 11)
                .takeUntil(Functions.remainderLong(128))
                .count();
        assertThat(count, is(7L));
    }
}
