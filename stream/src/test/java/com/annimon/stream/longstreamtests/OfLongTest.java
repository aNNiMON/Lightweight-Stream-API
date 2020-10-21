package com.annimon.stream.longstreamtests;

import com.annimon.stream.LongStream;
import org.junit.Test;
import static com.annimon.stream.test.hamcrest.LongStreamMatcher.elements;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.arrayContaining;

public final class OfLongTest {

    @Test
    public void testStreamOfLong() {
        assertThat(LongStream.of(1234), elements(arrayContaining(1234L)));
    }
}
