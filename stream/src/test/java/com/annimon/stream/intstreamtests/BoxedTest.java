package com.annimon.stream.intstreamtests;

import static com.annimon.stream.test.hamcrest.StreamMatcher.assertElements;
import static org.hamcrest.Matchers.contains;

import com.annimon.stream.IntStream;
import org.junit.Test;

public final class BoxedTest {

    @Test
    public void testBoxed() {
        IntStream.of(1, 10, 20).boxed().custom(assertElements(contains(1, 10, 20)));
    }
}
