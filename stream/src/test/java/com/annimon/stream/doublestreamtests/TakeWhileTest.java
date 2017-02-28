package com.annimon.stream.doublestreamtests;

import com.annimon.stream.DoubleStream;
import com.annimon.stream.Functions;
import org.junit.Test;
import static com.annimon.stream.test.hamcrest.DoubleStreamMatcher.assertElements;
import static com.annimon.stream.test.hamcrest.DoubleStreamMatcher.assertIsEmpty;
import static org.hamcrest.Matchers.arrayContaining;

public final class TakeWhileTest {

    @Test
    public void testTakeWhile() {
        DoubleStream.of(10.2, 30.234, 10.09, 2.2, 80d)
                .takeWhile(Functions.greaterThan(Math.PI))
                .custom(assertElements(arrayContaining(
                        10.2, 30.234, 10.09
                )));
    }

    @Test
    public void testTakeWhileNonFirstMatch() {
        DoubleStream.of(1.2, 3.234, 0.09, 2.2, 80d)
                .takeWhile(Functions.greaterThan(Math.PI))
                .custom(assertIsEmpty());
    }

    @Test
    public void testTakeWhileAllMatch() {
        DoubleStream.of(10.2, 30.234, 10.09)
                .takeWhile(Functions.greaterThan(Math.PI))
                .custom(assertElements(arrayContaining(
                        10.2, 30.234, 10.09
                )));
    }
}
