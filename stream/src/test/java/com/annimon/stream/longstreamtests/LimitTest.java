package com.annimon.stream.longstreamtests;

import com.annimon.stream.LongStream;
import org.junit.Test;
import static com.annimon.stream.test.hamcrest.LongStreamMatcher.assertElements;
import static com.annimon.stream.test.hamcrest.LongStreamMatcher.assertIsEmpty;
import static org.hamcrest.Matchers.arrayContaining;

public final class LimitTest {

    @Test
    public void testLimit() {
        LongStream.of(12L, 32L, 22L, 9L)
                .limit(2)
                .custom(assertElements(arrayContaining(
                        12L, 32L
                )));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testLimitNegative() {
        LongStream.of(12L, 32L).limit(-2).count();
    }

    @Test
    public void testLimitZero() {
        LongStream.of(12L, 32L)
                .limit(0)
                .custom(assertIsEmpty());
    }

    @Test
    public void testLimitMoreThanCount() {
        LongStream.of(12L, 32L, 22L)
                .limit(5)
                .custom(assertElements(arrayContaining(
                        12L, 32L, 22L
                )));
    }
}
