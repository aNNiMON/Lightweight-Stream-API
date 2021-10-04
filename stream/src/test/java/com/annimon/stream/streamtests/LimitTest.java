package com.annimon.stream.streamtests;

import static com.annimon.stream.test.hamcrest.StreamMatcher.assertElements;
import static com.annimon.stream.test.hamcrest.StreamMatcher.isEmpty;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;

import com.annimon.stream.Stream;
import org.junit.Test;

public final class LimitTest {

    @Test
    public void testLimit() {
        Stream.range(0, 10).limit(2).custom(assertElements(contains(0, 1)));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testLimitNegative() {
        Stream.range(0, 10).limit(-2).count();
    }

    @Test
    public void testLimitZero() {
        final Stream<Integer> stream = Stream.range(0, 10).limit(0);
        assertThat(stream, isEmpty());
    }

    @Test
    public void testLimitMoreThanCount() {
        Stream.range(0, 5).limit(15).custom(assertElements(contains(0, 1, 2, 3, 4)));
    }
}
