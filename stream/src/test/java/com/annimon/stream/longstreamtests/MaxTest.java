package com.annimon.stream.longstreamtests;

import com.annimon.stream.LongStream;
import com.annimon.stream.test.hamcrest.OptionalLongMatcher;
import org.junit.Test;
import static com.annimon.stream.test.hamcrest.OptionalLongMatcher.hasValue;
import static org.junit.Assert.assertThat;

public final class MaxTest {

    @Test
    public void testMax() {
        assertThat(LongStream.of(100, 20, 3).max(), hasValue(100L));
        assertThat(LongStream.empty().max(), OptionalLongMatcher.isEmpty());
    }
}
