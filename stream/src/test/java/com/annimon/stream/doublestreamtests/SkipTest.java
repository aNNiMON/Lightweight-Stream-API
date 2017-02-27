package com.annimon.stream.doublestreamtests;

import com.annimon.stream.DoubleStream;
import org.junit.Test;
import static com.annimon.stream.test.hamcrest.DoubleStreamMatcher.elements;
import static com.annimon.stream.test.hamcrest.DoubleStreamMatcher.isEmpty;
import static org.hamcrest.Matchers.arrayContaining;
import static org.junit.Assert.assertThat;

public final class SkipTest {

    @Test
    public void testSkip() {
        assertThat(DoubleStream.of(0.1, 0.2, 0.3).skip(2),
                elements(arrayContaining(0.3)));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSkipNegative() {
        DoubleStream.of(0.1, 2.345).skip(-2).count();
    }

    @Test
    public void testSkipZero() {
        assertThat(DoubleStream.of(0.1, 0.2, 0.3).skip(0),
                elements(arrayContaining(0.1, 0.2, 0.3)));
    }

    @Test
    public void testSkipMoreThanCount() {
        assertThat(DoubleStream.of(0.1, 0.2, 0.3).skip(5),
                isEmpty());
    }
}
