package com.annimon.stream.doublestreamtests;

import com.annimon.stream.DoubleStream;
import org.junit.Test;
import static com.annimon.stream.test.hamcrest.DoubleStreamMatcher.elements;
import static com.annimon.stream.test.hamcrest.DoubleStreamMatcher.isEmpty;
import static org.hamcrest.Matchers.arrayContaining;
import static org.junit.Assert.assertThat;

public final class LimitTest {

    @Test
    public void testLimit() {
        assertThat(DoubleStream.of(0.1, 0.2, 0.3).limit(2),
                elements(arrayContaining(0.1, 0.2)));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testLimitNegative() {
        DoubleStream.of(0.1, 2.345).limit(-2).count();
    }

    @Test
    public void testLimitZero() {
        assertThat(DoubleStream.of(0.1, 0.2, 0.3).limit(0),
                isEmpty());
    }

    @Test
    public void testLimitMoreThanCount() {
        assertThat(DoubleStream.of(0.1, 0.2, 0.3).limit(5),
                elements(arrayContaining(0.1, 0.2, 0.3)));
    }
}
