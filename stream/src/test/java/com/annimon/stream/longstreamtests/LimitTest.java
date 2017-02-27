package com.annimon.stream.longstreamtests;

import com.annimon.stream.LongStream;
import org.junit.Test;
import static com.annimon.stream.test.hamcrest.LongStreamMatcher.elements;
import static com.annimon.stream.test.hamcrest.LongStreamMatcher.isEmpty;
import static org.hamcrest.Matchers.arrayContaining;
import static org.junit.Assert.assertThat;

public final class LimitTest {

    @Test
    public void testLimit() {
        assertThat(LongStream.of(12L, 32L, 22L, 9L).limit(2),
                elements(arrayContaining(12L, 32L)));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testLimitNegative() {
        LongStream.of(12L, 32L).limit(-2).count();
    }

    @Test
    public void testLimitZero() {
        assertThat(LongStream.of(12L, 32L).limit(0),
                isEmpty());
    }

    @Test
    public void testLimitMoreThanCount() {
        assertThat(LongStream.of(12L, 32L, 22L).limit(5),
                elements(arrayContaining(12L, 32L, 22L)));
    }
}
