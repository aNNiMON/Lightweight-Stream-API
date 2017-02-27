package com.annimon.stream.streamtests;

import com.annimon.stream.Stream;
import org.junit.Test;
import static com.annimon.stream.test.hamcrest.StreamMatcher.isEmpty;
import static org.junit.Assert.assertThat;

public final class EmptyTest {

    @Test
    public void testStreamEmpty() {
        assertThat(Stream.empty(), isEmpty());
    }
}
