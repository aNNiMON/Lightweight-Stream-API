package com.annimon.stream.longstreamtests;

import com.annimon.stream.LongStream;
import org.junit.Test;
import static com.annimon.stream.test.hamcrest.LongStreamMatcher.isEmpty;
import static org.junit.Assert.assertThat;

public final class EmptyTest {

    @Test
    public void testStreamEmpty() {
        assertThat(LongStream.empty(), isEmpty());
    }
}
