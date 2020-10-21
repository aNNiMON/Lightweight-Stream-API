package com.annimon.stream.doublestreamtests;

import com.annimon.stream.DoubleStream;
import com.annimon.stream.function.DoublePredicate;
import com.annimon.stream.function.DoubleUnaryOperator;
import java.util.concurrent.atomic.AtomicInteger;
import org.junit.Test;
import static com.annimon.stream.test.hamcrest.DoubleStreamMatcher.assertElements;
import static org.hamcrest.Matchers.arrayContaining;
import static org.hamcrest.Matchers.closeTo;
import static org.junit.Assert.assertEquals;

public final class IterateTest {

    @Test
    @SuppressWarnings("unchecked")
    public void testStreamIterate() {
        DoubleUnaryOperator operator = new DoubleUnaryOperator() {
            @Override
            public double applyAsDouble(double operand) {
                return operand + 0.01;
            }
        };

        DoubleStream.iterate(0.0, operator)
                .limit(3)
                .custom(assertElements(arrayContaining(
                        closeTo(0.00, 0.00001),
                        closeTo(0.01, 0.00001),
                        closeTo(0.02, 0.00001)
                ))
        );
    }

    @SuppressWarnings("ConstantConditions")
    @Test(expected = NullPointerException.class)
    public void testStreamIterateNull() {
        DoubleStream.iterate(0, null);
    }

    @Test
    @SuppressWarnings("unchecked")
    public void testStreamIterateWithPredicate() {
        DoublePredicate condition = new DoublePredicate() {
            @Override
            public boolean test(double value) {
                return value < 0.2;
            }
        };
        DoubleUnaryOperator increment = new DoubleUnaryOperator() {
            @Override
            public double applyAsDouble(double t) {
                return t + 0.05;
            }
        };

        DoubleStream.iterate(0, condition, increment)
                .custom(assertElements(arrayContaining(
                        closeTo(0.00, 0.00001),
                        closeTo(0.05, 0.00001),
                        closeTo(0.10, 0.00001),
                        closeTo(0.15, 0.00001)
                ))
        );
    }

    @Test
    public void testIterateIssue186() {
        final AtomicInteger ai = new AtomicInteger(0);
        double result = DoubleStream.iterate(
                0,
                new DoublePredicate() {
                    @Override
                    public boolean test(double value) {
                        value = ai.incrementAndGet();
                        return value < 3;
                    }
                },
                DoubleUnaryOperator.Util.identity())
                .findFirst()
                .orElseThrow();
        assertEquals(0d, result, 0.1);
        assertEquals(1, ai.get());
    }
}
