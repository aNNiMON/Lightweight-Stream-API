package com.annimon.stream.longstreamtests;

import com.annimon.stream.LongStream;
import org.junit.Test;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public final class IteratorTest {

    @Test
    public void testIterator() {
        assertThat(LongStream.of(1234567L).iterator().nextLong(), is(1234567L));

        assertThat(LongStream.empty().iterator().hasNext(), is(false));

        assertThat(LongStream.empty().iterator().nextLong(), is(0L));
    }
}
