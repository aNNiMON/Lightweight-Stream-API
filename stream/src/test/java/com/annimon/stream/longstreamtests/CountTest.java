package com.annimon.stream.longstreamtests;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import com.annimon.stream.LongStream;
import org.junit.Test;

public final class CountTest {

    @Test
    public void testCount() {
        assertThat(LongStream.of(100, 20, 3).count(), is(3L));
        assertThat(LongStream.empty().count(), is(0L));
    }
}
