package com.annimon.stream.doublestreamtests;

import com.annimon.stream.DoubleStream;
import com.annimon.stream.Functions;
import org.junit.Test;
import static com.annimon.stream.test.hamcrest.DoubleStreamMatcher.elements;
import static org.hamcrest.Matchers.arrayContaining;
import static org.junit.Assert.assertThat;

public final class TakeUntilTest {

    @Test
    public void testTakeUntil() {
        DoubleStream stream;
        stream = DoubleStream.of(0.3, 2.2, 10.2, 30.234, 10.09, 80d)
                    .takeUntil(Functions.greaterThan(Math.PI));
        assertThat(stream, elements(arrayContaining(0.3, 2.2, 10.2)));
    }

    @Test
    public void testTakeUntilFirstMatch() {
        DoubleStream stream;
        stream = DoubleStream.of(11.2, 3.234, 0.09, 2.2, 80d)
                    .takeUntil(Functions.greaterThan(Math.PI));
        assertThat(stream, elements(arrayContaining(11.2)));
    }

    @Test
    public void testTakeUntilNoneMatch() {
        DoubleStream stream;
        stream = DoubleStream.of(1.2, 1.19, 0.09, 2.2)
                .takeUntil(Functions.greaterThan(128));
        assertThat(stream, elements(arrayContaining(1.2, 1.19, 0.09, 2.2)));
    }
}
