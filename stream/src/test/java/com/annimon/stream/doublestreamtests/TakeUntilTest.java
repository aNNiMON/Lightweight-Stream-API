package com.annimon.stream.doublestreamtests;

import com.annimon.stream.DoubleStream;
import com.annimon.stream.Functions;
import org.junit.Test;
import static com.annimon.stream.test.hamcrest.DoubleStreamMatcher.assertElements;
import static org.hamcrest.Matchers.arrayContaining;

public final class TakeUntilTest {

    @Test
    public void testTakeUntil() {
        DoubleStream.of(0.3, 2.2, 10.2, 30.234, 10.09, 80d)
                .takeUntil(Functions.greaterThan(Math.PI))
                .custom(assertElements(arrayContaining(
                        0.3, 2.2, 10.2
                )));
    }

    @Test
    public void testTakeUntilFirstMatch() {
        DoubleStream.of(11.2, 3.234, 0.09, 2.2, 80d)
                .takeUntil(Functions.greaterThan(Math.PI))
                .custom(assertElements(arrayContaining(
                        11.2
                )));
    }

    @Test
    public void testTakeUntilNoneMatch() {
        DoubleStream.of(1.2, 1.19, 0.09, 2.2)
                .takeUntil(Functions.greaterThan(128))
                .custom(assertElements(arrayContaining(
                        1.2, 1.19, 0.09, 2.2
                )));
    }
}
