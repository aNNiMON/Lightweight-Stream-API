package com.annimon.stream.longstreamtests;

import com.annimon.stream.LongStream;
import org.junit.Test;
import static com.annimon.stream.test.hamcrest.LongStreamMatcher.elements;
import static com.annimon.stream.test.hamcrest.LongStreamMatcher.isEmpty;
import static org.hamcrest.Matchers.arrayContaining;
import static org.junit.Assert.assertThat;

public final class SkipTest {

    @Test
    public void testSkip() {
        assertThat(LongStream.of(12L, 32L, 22L).skip(2),
                elements(arrayContaining(22L)));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSkipNegative() {
        LongStream.of(12L, 32L).skip(-2).count();
    }

    @Test
    public void testSkipZero() {
        assertThat(LongStream.of(12L, 32L, 22L).skip(0),
                elements(arrayContaining(12L, 32L, 22L)));
    }

    @Test
    public void testSkipMoreThanCount() {
        assertThat(LongStream.of(12L, 32L, 22L).skip(5),
                isEmpty());
    }
}
