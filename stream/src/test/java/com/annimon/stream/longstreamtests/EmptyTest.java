package com.annimon.stream.longstreamtests;

import static com.annimon.stream.test.hamcrest.LongStreamMatcher.isEmpty;
import static org.hamcrest.MatcherAssert.assertThat;

import com.annimon.stream.LongStream;
import org.junit.Test;

public final class EmptyTest {

    @Test
    public void testStreamEmpty() {
        assertThat(LongStream.empty(), isEmpty());
    }
}
