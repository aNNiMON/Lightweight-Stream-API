package com.annimon.stream.longstreamtests;

import static com.annimon.stream.test.hamcrest.StreamMatcher.assertElements;
import static org.hamcrest.Matchers.contains;

import com.annimon.stream.LongStream;
import org.junit.Test;

public final class BoxedTest {

    @Test
    public void testBoxed() {
        LongStream.of(10L, 20L, 30L).boxed().custom(assertElements(contains(10L, 20L, 30L)));
    }
}
