package com.annimon.stream.intstreamtests;

import com.annimon.stream.IntStream;
import com.annimon.stream.function.IntBinaryOperator;
import org.junit.Test;
import static com.annimon.stream.test.hamcrest.IntStreamMatcher.assertElements;
import static com.annimon.stream.test.hamcrest.IntStreamMatcher.assertIsEmpty;
import static org.hamcrest.Matchers.arrayContaining;

public final class ScanTest {

    @Test
    public void testScan() {
        IntStream.of(1, 2, 3)
                .scan(new IntBinaryOperator() {
                    @Override
                    public int applyAsInt(int left, int right) {
                        return left + right;
                    }
                })
                .custom(assertElements(arrayContaining(
                        1, 3, 6
                )));
    }

    @Test
    public void testScanNonAssociative() {
        IntStream.of(1800, 2, 3, 5)
                .scan(new IntBinaryOperator() {
                    @Override
                    public int applyAsInt(int value1, int value2) {
                        return value1 / value2;
                    }
                })
                .custom(assertElements(arrayContaining(
                        1800,
                        1800 / 2,
                        1800 / 2 / 3,
                        1800 / 2 / 3 / 5
                )));
    }

    @Test
    public void testScanOnEmptyStream() {
        IntStream.empty()
                .scan(new IntBinaryOperator() {
                    @Override
                    public int applyAsInt(int left, int right) {
                        return left + right;
                    }
                })
                .custom(assertIsEmpty());
    }

    @Test
    public void testScanWithIdentity() {
        IntStream.of(1, 2, 3)
                .scan(0, new IntBinaryOperator() {
                    @Override
                    public int applyAsInt(int left, int right) {
                        return left + right;
                    }
                })
                .custom(assertElements(arrayContaining(
                        0, 1, 3, 6
                )));
    }

    @Test
    public void testScanWithIdentityNonAssociative() {
        IntStream.of(2, 3, 5)
                .scan(1800, new IntBinaryOperator() {
                    @Override
                    public int applyAsInt(int value1, int value2) {
                        return value1 / value2;
                    }
                })
                .custom(assertElements(arrayContaining(
                        1800,
                        1800 / 2,
                        1800 / 2 / 3,
                        1800 / 2 / 3 / 5
                )));
    }

    @Test
    public void testScanWithIdentityOnEmptyStream() {
        IntStream.empty()
                .scan(9, new IntBinaryOperator() {
                    @Override
                    public int applyAsInt(int left, int right) {
                        return left + right;
                    }
                })
                .custom(assertElements(arrayContaining(
                        9
                )));
    }
}
