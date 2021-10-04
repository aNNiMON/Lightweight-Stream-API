package com.annimon.stream.streamtests;

import static com.annimon.stream.test.hamcrest.StreamMatcher.isEmpty;
import static org.hamcrest.MatcherAssert.assertThat;

import com.annimon.stream.Stream;
import org.junit.Test;

public final class EmptyTest {

    @Test
    public void testStreamEmpty() {
        assertThat(Stream.empty(), isEmpty());
    }
}
