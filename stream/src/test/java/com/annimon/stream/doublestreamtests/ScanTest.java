package com.annimon.stream.doublestreamtests;

import com.annimon.stream.DoubleStream;
import com.annimon.stream.function.DoubleBinaryOperator;
import org.junit.Test;
import static com.annimon.stream.test.hamcrest.DoubleStreamMatcher.assertElements;
import static com.annimon.stream.test.hamcrest.DoubleStreamMatcher.assertIsEmpty;
import static org.hamcrest.Matchers.arrayContaining;
import static org.hamcrest.Matchers.closeTo;
import static org.hamcrest.Matchers.is;

public final class ScanTest {

    @Test
    @SuppressWarnings("unchecked")
    public void testScan() {
        DoubleStream.of(1.1, 2.2, 3.3)
                .scan(new DoubleBinaryOperator() {
                    @Override
                    public double applyAsDouble(double left, double right) {
                        return left + right;
                    }
                })
                .custom(assertElements(arrayContaining(
                        closeTo(1.1, 0.00001),
                        closeTo(3.3, 0.00001),
                        closeTo(6.6, 0.00001)
                )));
    }

    @Test
    @SuppressWarnings("unchecked")
    public void testScanNonAssociative() {
        DoubleStream.of(1.0, 0.2, 0.3, 0.5)
                .scan(new DoubleBinaryOperator() {
                    @Override
                    public double applyAsDouble(double value1, double value2) {
                        return value1 / value2;
                    }
                })
                .custom(assertElements(is(arrayContaining(
                        closeTo(1.0, 0.00001),
                        closeTo(1.0 / 0.2, 0.00001),
                        closeTo(1.0 / 0.2 / 0.3, 0.00001),
                        closeTo(1.0 / 0.2 / 0.3 / 0.5, 0.00001)
                ))));
    }

    @Test
    public void testScanOnEmptyStream() {
        DoubleStream.empty()
                .scan(new DoubleBinaryOperator() {
                    @Override
                    public double applyAsDouble(double left, double right) {
                        return left + right;
                    }
                })
                .custom(assertIsEmpty());
    }

    @Test
    @SuppressWarnings("unchecked")
    public void testScanWithIdentity() {
        DoubleStream.of(2.2, 3.3, 1.1)
                .scan(1.1, new DoubleBinaryOperator() {
                    @Override
                    public double applyAsDouble(double left, double right) {
                        return left + right;
                    }
                })
                .custom(assertElements(arrayContaining(
                        closeTo(1.1, 0.00001),
                        closeTo(3.3, 0.00001),
                        closeTo(6.6, 0.00001),
                        closeTo(7.7, 0.00001)
                )));
    }

    @Test
    @SuppressWarnings("unchecked")
    public void testScanWithIdentityNonAssociative() {
        DoubleStream.of(0.2, 0.3, 0.5)
                .scan(1d, new DoubleBinaryOperator() {
                    @Override
                    public double applyAsDouble(double value1, double value2) {
                        return value1 / value2;
                    }
                })
                .custom(assertElements(arrayContaining(
                        closeTo(1.0, 0.00001),
                        closeTo(1.0 / 0.2, 0.00001),
                        closeTo(1.0 / 0.2 / 0.3, 0.00001),
                        closeTo(1.0 / 0.2 / 0.3 / 0.5, 0.00001)
                )));
    }

    @Test
    public void testScanWithIdentityOnEmptyStream() {
        DoubleStream.empty()
                .scan(10.09, new DoubleBinaryOperator() {
                    @Override
                    public double applyAsDouble(double left, double right) {
                        return left + right;
                    }
                })
                .custom(assertElements(arrayContaining(
                        10.09
                )));
    }
}
