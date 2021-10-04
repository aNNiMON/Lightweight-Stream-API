package com.annimon.stream.longstreamtests;

import static com.annimon.stream.test.hamcrest.OptionalLongMatcher.hasValue;
import static com.annimon.stream.test.hamcrest.OptionalLongMatcher.isEmpty;
import static org.hamcrest.MatcherAssert.assertThat;

import com.annimon.stream.LongStream;
import org.junit.Test;

public final class MaxTest {

    @Test
    public void testMax() {
        assertThat(LongStream.of(100, 20, 3).max(), hasValue(100L));
        assertThat(LongStream.empty().max(), isEmpty());
    }
}
