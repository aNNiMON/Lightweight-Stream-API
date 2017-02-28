package com.annimon.stream.longstreamtests;

import com.annimon.stream.LongStream;
import org.junit.Test;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public final class ToArrayTest {

    @Test
    public void testToArray() {
        assertThat(LongStream.of(12L, 32L, 22L, 9L).toArray(),
                is(new long[] {12L, 32L, 22L, 9L}));
    }

    @Test
    public void testToArrayOnEmptyStream() {
        assertThat(LongStream.empty().toArray().length, is(0));
    }
}
