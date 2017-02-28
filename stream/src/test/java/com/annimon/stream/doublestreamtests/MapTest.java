package com.annimon.stream.doublestreamtests;

import com.annimon.stream.DoubleStream;
import com.annimon.stream.function.DoubleUnaryOperator;
import org.junit.Test;
import static com.annimon.stream.test.hamcrest.DoubleStreamMatcher.assertElements;
import static com.annimon.stream.test.hamcrest.DoubleStreamMatcher.assertIsEmpty;
import static org.hamcrest.Matchers.arrayContaining;

public final class MapTest {

    @Test
    public void testMap() {
        DoubleUnaryOperator negator = new DoubleUnaryOperator() {
            @Override
            public double applyAsDouble(double operand) {
                return -operand;
            }
        };
        DoubleStream.of(0.012, 3.039, 100d)
                .map(negator)
                .custom(assertElements(arrayContaining(
                        -0.012, -3.039, -100d
                )));

        DoubleStream.empty()
                .map(negator)
                .custom(assertIsEmpty());
    }
}
