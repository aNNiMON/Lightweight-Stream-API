package com.annimon.stream.streamtests;

import com.annimon.stream.Stream;
import org.junit.Test;
import static com.annimon.stream.test.hamcrest.StreamMatcher.assertElements;
import static org.hamcrest.Matchers.contains;

public final class SampleTest {

    @Test
    public void testSample() {
        Stream.of(      1, 2, 3, 1, 2, 3, 1, 2, 3)
                .sample(3)
                .custom(assertElements(contains(
                        1,       1,       1
                )));
    }

    @Test
    public void testSampleWithStep1() {
        Stream.of(      1, 2, 3, 1, 2, 3, 1, 2, 3)
                .sample(1)
                .custom(assertElements(contains(
                        1, 2, 3, 1, 2, 3, 1, 2, 3
                )));
    }

    @Test(expected = IllegalArgumentException.class, timeout=1000)
    public void testSampleWithNegativeStep() {
        Stream.of(1, 2, 3, 1, 2, 3, 1, 2, 3).sample(-1).count();
    }
}
