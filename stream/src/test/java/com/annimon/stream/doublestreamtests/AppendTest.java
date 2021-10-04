package com.annimon.stream.doublestreamtests;

import static com.annimon.stream.test.hamcrest.DoubleStreamMatcher.assertElements;
import static org.hamcrest.Matchers.arrayContaining;

import com.annimon.stream.DoubleStream;
import com.annimon.stream.Functions;
import com.annimon.stream.test.hamcrest.DoubleStreamMatcher;
import org.junit.Test;

public final class AppendTest {

    @Test
    public void testAppendEmptyStream() {
        DoubleStream.empty()
                .append(DoubleStream.empty())
                .custom(DoubleStreamMatcher.assertIsEmpty());

        DoubleStream.of(1, 2)
                .append(DoubleStream.empty())
                .custom(assertElements(arrayContaining(1d, 2d)));
    }

    @Test
    public void testAppend() {
        DoubleStream.empty().append(DoubleStream.of(0)).custom(assertElements(arrayContaining(0d)));
        DoubleStream.of(1, 2)
                .append(DoubleStream.of(0))
                .custom(assertElements(arrayContaining(1d, 2d, 0d)));
    }

    @Test
    public void testAppendAfterFiltering() {
        DoubleStream.of(1, 2, 3, 4, 5, 6)
                .filter(Functions.greaterThan(3))
                .append(DoubleStream.of(1))
                .custom(assertElements(arrayContaining(4d, 5d, 6d, 1d)));
        DoubleStream.of(1, 2, 3, 4, 5, 6)
                .filter(Functions.greaterThan(3))
                .append(DoubleStream.of(1, 2, 3, 4).filterNot(Functions.greaterThan(3)))
                .custom(assertElements(arrayContaining(4d, 5d, 6d, 1d, 2d, 3d)));
    }

    @Test
    public void testManyAppends() {
        DoubleStream.of(1)
                .append(DoubleStream.of(2))
                .append(DoubleStream.of(3))
                .append(DoubleStream.of(4))
                .custom(assertElements(arrayContaining(1d, 2d, 3d, 4d)));
    }

    @Test
    public void testManyAppendsComplex() {
        DoubleStream.of(1, 2, 3, 4, 5, 6)
                .filter(Functions.greaterThan(2))
                .append(DoubleStream.of(2, 3, 4, 5))
                .filter(Functions.greaterThan(3))
                .limit(4)
                .append(DoubleStream.of(6, 7, 8, 9, 10).filter(Functions.greaterThan(8)))
                .custom(assertElements(arrayContaining(4d, 5d, 6d, 4d, 9d, 10d)));
    }
}
