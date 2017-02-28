package com.annimon.stream.doublestreamtests;

import com.annimon.stream.DoubleStream;
import com.annimon.stream.Functions;
import org.junit.Test;
import static com.annimon.stream.test.hamcrest.DoubleStreamMatcher.assertElements;
import static com.annimon.stream.test.hamcrest.DoubleStreamMatcher.assertIsEmpty;
import static org.hamcrest.Matchers.arrayContaining;

public final class DropWhileTest {

    @Test
    public void testDropWhile() {
        DoubleStream.of(10.2, 30.234, 10.09, 2.2, 80d)
                .dropWhile(Functions.greaterThan(Math.PI))
                .custom(assertElements(arrayContaining(
                        2.2, 80d
                )));
    }
    @Test
    public void testDropWhileNonFirstMatch() {
        DoubleStream.of(1.2, 3.234, 0.09, 2.2, 80d)
                .dropWhile(Functions.greaterThan(Math.PI))
                .custom(assertElements(arrayContaining(
                        1.2, 3.234, 0.09, 2.2, 80d
                )));
    }

    @Test
    public void testDropWhileAllMatch() {
        DoubleStream.of(10.2, 30.234, 80d)
                .dropWhile(Functions.greaterThan(Math.PI))
                .custom(assertIsEmpty());
    }
}
