package com.annimon.stream.intstreamtests;

import com.annimon.stream.IntStream;
import org.junit.Test;
import static com.annimon.stream.test.hamcrest.StreamMatcher.assertElements;
import static org.hamcrest.Matchers.contains;

public final class BoxedTest {

    @Test
    public void testBoxed() {
        IntStream.of(1, 10, 20)
                .boxed()
                .custom(assertElements(contains(
                        1, 10, 20
                )));
    }
}
