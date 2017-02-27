package com.annimon.stream.doublestreamtests;

import com.annimon.stream.DoubleStream;
import org.junit.Test;
import static com.annimon.stream.test.hamcrest.DoubleStreamMatcher.elements;
import static com.annimon.stream.test.hamcrest.DoubleStreamMatcher.isEmpty;
import static org.hamcrest.Matchers.arrayContaining;
import static org.junit.Assert.assertThat;

public final class ConcatTest {

     @Test
    public void testStreamConcat() {
        DoubleStream a1 = DoubleStream.empty();
        DoubleStream b1 = DoubleStream.empty();
        assertThat(DoubleStream.concat(a1, b1), isEmpty());

        DoubleStream a2 = DoubleStream.of(10.123, Math.PI);
        DoubleStream b2 = DoubleStream.empty();
        assertThat(DoubleStream.concat(a2, b2),
                elements(arrayContaining(10.123, Math.PI)));

        DoubleStream a3 = DoubleStream.of(10.123, Math.PI);
        DoubleStream b3 = DoubleStream.empty();
        assertThat(DoubleStream.concat(a3, b3),
                elements(arrayContaining(10.123, Math.PI)));

        DoubleStream a4 = DoubleStream.of(10.123, Math.PI, -1e11, -1e8);
        DoubleStream b4 = DoubleStream.of(1.617, 9.81);
        assertThat(DoubleStream.concat(a4, b4),
                elements(arrayContaining(10.123, Math.PI, -1e11, -1e8, 1.617, 9.81)));
    }

    @Test(expected = NullPointerException.class)
    public void testStreamConcatNullA() {
        DoubleStream.concat(null, DoubleStream.empty());
    }

    @Test(expected = NullPointerException.class)
    public void testStreamConcatNullB() {
        DoubleStream.concat(DoubleStream.empty(), null);
    }
}
