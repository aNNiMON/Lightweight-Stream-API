package com.annimon.stream.longstreamtests;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import com.annimon.stream.LongStream;
import org.junit.Test;

public final class ToArrayTest {

    @Test
    public void testToArray() {
        assertThat(LongStream.of(12L, 32L, 22L, 9L).toArray(), is(new long[] {12L, 32L, 22L, 9L}));
    }

    @Test
    public void testToArrayOnEmptyStream() {
        assertThat(LongStream.empty().toArray().length, is(0));
    }
}
