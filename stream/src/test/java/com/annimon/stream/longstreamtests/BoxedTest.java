package com.annimon.stream.longstreamtests;

import com.annimon.stream.LongStream;
import com.annimon.stream.test.hamcrest.StreamMatcher;
import java.util.Arrays;
import org.junit.Test;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public final class BoxedTest {

    @Test
    public void testBoxed() {
        assertThat(LongStream.of(10L, 20L, 30L).boxed(),
                StreamMatcher.elements(is(Arrays.asList(10L, 20L, 30L))));
    }
}
