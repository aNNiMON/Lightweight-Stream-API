package com.annimon.stream.doublestreamtests;

import com.annimon.stream.Functions;
import com.annimon.stream.DoubleStream;
import com.annimon.stream.test.hamcrest.DoubleStreamMatcher;
import org.junit.Test;
import static com.annimon.stream.test.hamcrest.DoubleStreamMatcher.assertElements;
import static org.hamcrest.Matchers.arrayContaining;

public final class PrependTest {

    @Test
    public void testPrependEmptyStream() {
        DoubleStream.empty()
                .prepend(DoubleStream.empty())
                .custom(DoubleStreamMatcher.assertIsEmpty());

        DoubleStream.of(1, 2)
                .prepend(DoubleStream.empty())
                .custom(assertElements(arrayContaining(
                        1d, 2d
                )));
    }

    @Test
    public void testPrepend() {
        DoubleStream.empty()
                .prepend(DoubleStream.of(0))
                .custom(assertElements(arrayContaining(
                        0d
                )));
        DoubleStream.of(1, 2)
                .prepend(DoubleStream.of(0))
                .custom(assertElements(arrayContaining(
                        0d, 1d, 2d
                )));
    }

    @Test
    public void testPrependAfterFiltering() {
        DoubleStream.of(1, 2, 3, 4, 5)
                .filter(Functions.greaterThan(3))
                .prepend(DoubleStream.of(1))
                .custom(assertElements(arrayContaining(
                        1d, 4d, 5d
                )));
        DoubleStream.of(1, 2, 3, 4, 5)
                .filter(Functions.greaterThan(3))
                .prepend(DoubleStream.of(1, 2, 3, 4).filterNot(Functions.greaterThan(2)))
                .custom(assertElements(arrayContaining(
                        1d, 2d, 4d, 5d
                )));
    }

    @Test
    public void testManyPrepends() {
        DoubleStream.of(1)
                .prepend(DoubleStream.of(2))
                .prepend(DoubleStream.of(3))
                .prepend(DoubleStream.of(4))
                .custom(assertElements(arrayContaining(
                        4d, 3d, 2d, 1d
                )));
    }

    @Test
    public void testManyPrependsComplex() {
        DoubleStream.of(1, 2, 3, 4, 5, 6)
                .filter(Functions.greaterThan(2))
                .prepend(DoubleStream.of(2, 3, 4, 5))
                .filter(Functions.greaterThan(3))
                .limit(4)
                .prepend(DoubleStream.of(6, 7, 8, 9, 10).filter(Functions.greaterThan(8)))
                .custom(assertElements(arrayContaining(
                        9d, 10d, 4d, 5d, 4d, 5d
                )));
    }
}
