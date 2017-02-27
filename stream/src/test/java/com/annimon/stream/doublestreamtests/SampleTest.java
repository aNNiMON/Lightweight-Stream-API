package com.annimon.stream.doublestreamtests;

import com.annimon.stream.DoubleStream;
import org.junit.Test;
import static com.annimon.stream.test.hamcrest.DoubleStreamMatcher.elements;
import static org.hamcrest.Matchers.arrayContaining;
import static org.junit.Assert.assertThat;

public final class SampleTest {

    @Test
    public void testSample() {
        assertThat(DoubleStream.of(1.2, 3.234, 0.09, 2.2, 80d).sample(2),
                elements(arrayContaining(1.2, 0.09, 80d)));
    }

    @Test
    public void testSampleWithStep1() {
        assertThat(DoubleStream.of(1.2, 3.234, 0.09, 2.2, 80d).sample(1),
                elements(arrayContaining(1.2, 3.234, 0.09, 2.2, 80d)));
    }

    @Test(expected = IllegalArgumentException.class, timeout=1000)
    public void testSampleWithNegativeStep() {
        DoubleStream.of(1.2, 3.234).sample(-1).count();
    }
}
