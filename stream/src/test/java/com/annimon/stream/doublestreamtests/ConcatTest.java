package com.annimon.stream.doublestreamtests;

import com.annimon.stream.DoubleStream;
import org.junit.Test;
import static com.annimon.stream.test.hamcrest.DoubleStreamMatcher.assertElements;
import static com.annimon.stream.test.hamcrest.DoubleStreamMatcher.isEmpty;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.arrayContaining;

public final class ConcatTest {

     @Test
    public void testStreamConcat() {
        DoubleStream a1 = DoubleStream.empty();
        DoubleStream b1 = DoubleStream.empty();
        assertThat(DoubleStream.concat(a1, b1), isEmpty());

        DoubleStream a2 = DoubleStream.of(10.123, Math.PI);
        DoubleStream b2 = DoubleStream.empty();
        DoubleStream.concat(a2, b2)
                .custom(assertElements(arrayContaining(
                        10.123, Math.PI
                )));

        DoubleStream a3 = DoubleStream.of(10.123, Math.PI);
        DoubleStream b3 = DoubleStream.empty();
        DoubleStream.concat(a3, b3)
                .custom(assertElements(arrayContaining(
                        10.123, Math.PI
                )));

        DoubleStream a4 = DoubleStream.of(10.123, Math.PI, -1e11, -1e8);
        DoubleStream b4 = DoubleStream.of(1.617, 9.81);
        DoubleStream.concat(a4, b4)
                .custom(assertElements(arrayContaining(
                        10.123, Math.PI, -1e11, -1e8, 1.617, 9.81
                )));
    }

    @SuppressWarnings("ConstantConditions")
    @Test(expected = NullPointerException.class)
    public void testStreamConcatNullA() {
        DoubleStream.concat(null, DoubleStream.empty());
    }

    @SuppressWarnings("ConstantConditions")
    @Test(expected = NullPointerException.class)
    public void testStreamConcatNullB() {
        DoubleStream.concat(DoubleStream.empty(), null);
    }

    @Test
    public void testConcat3Streams() {
        DoubleStream a1 = DoubleStream.of(1, 2, 3);
        DoubleStream b1 = DoubleStream.of(4);
        DoubleStream c1 = DoubleStream.of(5, 6);
        DoubleStream.concat(a1, b1, c1)
                .custom(assertElements(arrayContaining(
                        1d, 2d, 3d,
                        4d,
                        5d, 6d
                )));

        DoubleStream a2 = DoubleStream.of(1, 2, 3);
        DoubleStream b2 = DoubleStream.empty();
        DoubleStream c2 = DoubleStream.of(5, 6);
        DoubleStream.concat(a2, b2, c2)
                .custom(assertElements(arrayContaining(
                        1d, 2d, 3d,
                        // empty
                        5d, 6d
                )));

        DoubleStream a3 = DoubleStream.empty();
        DoubleStream b3 = DoubleStream.empty();
        DoubleStream c3 = DoubleStream.empty();
        assertThat(DoubleStream.concat(a3, b3, c3), isEmpty());
    }
}
