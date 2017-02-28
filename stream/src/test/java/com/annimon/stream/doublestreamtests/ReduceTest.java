package com.annimon.stream.doublestreamtests;

import com.annimon.stream.DoubleStream;
import com.annimon.stream.function.DoubleBinaryOperator;
import org.junit.Test;
import static com.annimon.stream.test.hamcrest.OptionalDoubleMatcher.hasValueThat;
import static com.annimon.stream.test.hamcrest.OptionalDoubleMatcher.isEmpty;
import static org.hamcrest.Matchers.closeTo;
import static org.junit.Assert.assertThat;

public final class ReduceTest {

    @Test
    public void testReduceWithIdentity() {
        double result = DoubleStream.of(0.012, -3.772, 3.039, 19.84, 100d)
                .reduce(0d, new DoubleBinaryOperator() {
            @Override
            public double applyAsDouble(double left, double right) {
                return left + right;
            }
        });
        assertThat(result, closeTo(119.119, 0.0001));
    }

    @Test
    public void testReduceWithIdentityOnEmptyStream() {
        double result = DoubleStream.empty().reduce(Math.PI, new DoubleBinaryOperator() {
            @Override
            public double applyAsDouble(double left, double right) {
                return left + right;
            }
        });
        assertThat(result, closeTo(Math.PI, 0.00001));
    }

    @Test
    public void testReduce() {
        assertThat(DoubleStream.of(0.012, -3.772, 3.039, 19.84, 100d)
                .reduce(new DoubleBinaryOperator() {
            @Override
            public double applyAsDouble(double left, double right) {
                return left + right;
            }
        }), hasValueThat(closeTo(119.119, 0.0001)));
    }

    @Test
    public void testReduceOnEmptyStream() {
        assertThat(DoubleStream.empty().reduce(new DoubleBinaryOperator() {
            @Override
            public double applyAsDouble(double left, double right) {
                return left + right;
            }
        }), isEmpty());
    }
}
