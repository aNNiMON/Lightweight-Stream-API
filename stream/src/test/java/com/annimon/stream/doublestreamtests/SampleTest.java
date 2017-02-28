package com.annimon.stream.doublestreamtests;

import com.annimon.stream.DoubleStream;
import org.junit.Test;
import static com.annimon.stream.test.hamcrest.DoubleStreamMatcher.assertElements;
import static org.hamcrest.Matchers.arrayContaining;

public final class SampleTest {

    @Test
    public void testSample() {
        DoubleStream.of(1.2, 3.234, 0.09, 2.2, 80d)
                .sample(2)
                .custom(assertElements(arrayContaining(
                        1.2, 0.09, 80d
                )));
    }

    @Test
    public void testSampleWithStep1() {
        DoubleStream.of(1.2, 3.234, 0.09, 2.2, 80d)
                .sample(1)
                .custom(assertElements(arrayContaining(
                        1.2, 3.234, 0.09, 2.2, 80d
                )));
    }

    @Test(expected = IllegalArgumentException.class, timeout=1000)
    public void testSampleWithNegativeStep() {
        DoubleStream.of(1.2, 3.234).sample(-1).count();
    }
}
