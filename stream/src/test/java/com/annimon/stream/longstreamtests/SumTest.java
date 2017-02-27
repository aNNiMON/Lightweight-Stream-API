package com.annimon.stream.longstreamtests;

import com.annimon.stream.LongStream;
import org.junit.Test;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public final class SumTest {

    @Test
    public void testSum() {
        assertThat(LongStream.of(100, 20, 3).sum(), is(123L));
        assertThat(LongStream.empty().sum(), is(0L));
    }
}
