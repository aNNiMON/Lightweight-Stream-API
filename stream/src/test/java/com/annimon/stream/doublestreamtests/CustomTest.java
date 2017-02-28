package com.annimon.stream.doublestreamtests;

import com.annimon.stream.CustomOperators;
import com.annimon.stream.DoubleStream;
import com.annimon.stream.IntStream;
import com.annimon.stream.function.DoubleBinaryOperator;
import org.junit.Test;
import static com.annimon.stream.test.hamcrest.DoubleStreamMatcher.elements;
import static org.hamcrest.Matchers.arrayContaining;
import static org.hamcrest.Matchers.closeTo;
import static org.junit.Assert.assertThat;

public final class CustomTest {

    @Test(expected = NullPointerException.class)
    public void testCustom() {
        DoubleStream.empty().custom(null);
    }

    @Test
    @SuppressWarnings("unchecked")
    public void testCustomIntermediateOperator_Zip() {
        final DoubleBinaryOperator op = new DoubleBinaryOperator() {
            @Override
            public double applyAsDouble(double left, double right) {
                return left * right;
            }
        };
        DoubleStream s1 = DoubleStream.of(1.01, 2.02, 3.03);
        IntStream s2 = IntStream.range(2, 5);
        DoubleStream result = s1.custom(new CustomOperators.ZipWithIntStream(s2, op));
        assertThat(result, elements(arrayContaining(
                closeTo(2.02, 0.00001),
                closeTo(6.06, 0.00001),
                closeTo(12.12, 0.00001)
        )));
    }

    @Test
    public void testCustomTerminalOperator_DoubleSummaryStatistics() {
        double[] result = DoubleStream.of(0.1, 0.02, 0.003).custom(new CustomOperators.DoubleSummaryStatistics());
        double count = result[0], sum = result[1], average = result[2];
        assertThat(count, closeTo(3, 0.0001));
        assertThat(sum, closeTo(0.123, 0.0001));
        assertThat(average, closeTo(0.041, 0.0001));
    }
}
