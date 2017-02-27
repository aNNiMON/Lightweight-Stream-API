package com.annimon.stream.doublestreamtests;

import com.annimon.stream.DoubleStream;
import com.annimon.stream.Functions;
import org.junit.Test;
import static com.annimon.stream.test.hamcrest.DoubleStreamMatcher.elements;
import static com.annimon.stream.test.hamcrest.DoubleStreamMatcher.isEmpty;
import static org.hamcrest.Matchers.arrayContaining;
import static org.junit.Assert.assertThat;

public final class TakeWhileTest {

    @Test
    public void testTakeWhile() {
        DoubleStream stream;
        stream = DoubleStream.of(10.2, 30.234, 10.09, 2.2, 80d)
                    .takeWhile(Functions.greaterThan(Math.PI));
        assertThat(stream, elements(arrayContaining(10.2, 30.234, 10.09)));
    }

    @Test
    public void testTakeWhileNonFirstMatch() {
        DoubleStream stream;
        stream = DoubleStream.of(1.2, 3.234, 0.09, 2.2, 80d)
                    .takeWhile(Functions.greaterThan(Math.PI));
        assertThat(stream, isEmpty());
    }

    @Test
    public void testTakeWhileAllMatch() {
        DoubleStream stream;
        stream = DoubleStream.of(10.2, 30.234, 10.09)
                    .takeWhile(Functions.greaterThan(Math.PI));
        assertThat(stream, elements(arrayContaining(10.2, 30.234, 10.09)));
    }
}
