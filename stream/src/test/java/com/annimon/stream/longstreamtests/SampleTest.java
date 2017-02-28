package com.annimon.stream.longstreamtests;

import com.annimon.stream.LongStream;
import org.junit.Test;
import static com.annimon.stream.test.hamcrest.LongStreamMatcher.assertElements;
import static org.hamcrest.Matchers.arrayContaining;

public final class SampleTest {

    @Test
    public void testSample() {
        LongStream.of(12, 32, 9, 22, 41, 42)
                .sample(2)
                .custom(assertElements(arrayContaining(
                        12L, 9L, 41L
                )));

        LongStream.of(12, 32, 9, 22, 41, 42)
                .skip(1)
                .sample(2)
                .custom(assertElements(arrayContaining(
                        32L, 22L, 42L
                )));
    }

    @Test
    public void testSampleWithStep1() {
        LongStream.of(12, 32, 9, 22, 41, 42)
                .sample(1)
                .custom(assertElements(arrayContaining(
                        12L, 32L, 9L, 22L, 41L, 42L
                )));
    }

    @Test(expected = IllegalArgumentException.class, timeout=1000)
    public void testSampleWithNegativeStep() {
        LongStream.of(12, 34).sample(-1).count();
    }
}
