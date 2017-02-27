package com.annimon.stream.doublestreamtests;

import com.annimon.stream.DoubleStream;
import com.annimon.stream.function.DoubleUnaryOperator;
import org.junit.Test;
import static com.annimon.stream.test.hamcrest.DoubleStreamMatcher.elements;
import static com.annimon.stream.test.hamcrest.DoubleStreamMatcher.isEmpty;
import static org.hamcrest.Matchers.arrayContaining;
import static org.junit.Assert.assertThat;

public final class MapTest {

    @Test
    public void testMap() {
        DoubleUnaryOperator negator = new DoubleUnaryOperator() {
            @Override
            public double applyAsDouble(double operand) {
                return -operand;
            }
        };
        assertThat(DoubleStream.of(0.012, 3.039, 100d).map(negator),
                elements(arrayContaining(-0.012, -3.039, -100d)));

        assertThat(DoubleStream.empty().map(negator),
                isEmpty());
    }
}
