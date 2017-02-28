package com.annimon.stream.doublestreamtests;

import com.annimon.stream.DoubleStream;
import org.junit.Test;
import static com.annimon.stream.test.hamcrest.StreamMatcher.assertElements;
import static org.hamcrest.Matchers.contains;

public final class BoxedTest {

    @Test
    public void testBoxed() {
        DoubleStream.of(0.1, 0.2, 0.3)
                .boxed()
                .custom(assertElements(contains(
                        0.1, 0.2, 0.3
                )));
    }
}
