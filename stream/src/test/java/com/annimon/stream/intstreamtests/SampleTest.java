package com.annimon.stream.intstreamtests;

import com.annimon.stream.IntStream;
import org.junit.Test;
import static com.annimon.stream.test.hamcrest.IntStreamMatcher.assertElements;
import static org.hamcrest.Matchers.arrayContaining;

public final class SampleTest {

    @Test
    public void testSample() {
        IntStream.of(   1, 2, 3, 1, 2, 3, 1, 2, 3)
                .sample(3)
                .custom(assertElements(arrayContaining(
                        1,       1,       1
                )));
    }

    @Test
    public void testSampleWithStep1() {
        IntStream.of(   1, 2, 3, 1, 2, 3, 1, 2, 3)
                .sample(1)
                .custom(assertElements(arrayContaining(
                        1, 2, 3, 1, 2, 3, 1, 2, 3
                )));
    }

    @Test(expected = IllegalArgumentException.class, timeout=1000)
    public void testSampleWithNegativeStep() {
        IntStream.of(1, 2, 3, 1, 2, 3, 1, 2, 3).sample(-1).count();
    }
}
