package com.annimon.stream.longstreamtests;

import com.annimon.stream.LongStream;
import org.junit.Test;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public final class CountTest {

    @Test
    public void testCount() {
        assertThat(LongStream.of(100, 20, 3).count(), is(3L));
        assertThat(LongStream.empty().count(), is(0L));
    }
}
