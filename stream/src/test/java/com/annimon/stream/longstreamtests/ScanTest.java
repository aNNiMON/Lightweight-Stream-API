package com.annimon.stream.longstreamtests;

import com.annimon.stream.LongStream;
import com.annimon.stream.function.LongBinaryOperator;
import org.junit.Test;
import static com.annimon.stream.test.hamcrest.LongStreamMatcher.assertElements;
import static com.annimon.stream.test.hamcrest.LongStreamMatcher.assertIsEmpty;
import static org.hamcrest.Matchers.arrayContaining;

public final class ScanTest {

    @Test
    public void testScan() {
        LongStream.of(1, 2, 3)
                .scan(new LongBinaryOperator() {
                    @Override
                    public long applyAsLong(long left, long right) {
                        return left + right;
                    }
                })
                .custom(assertElements(arrayContaining(
                        1L, 3L, 6L
                )));
    }

    @Test
    public void testScanNonAssociative() {
        LongStream.of(1800L, 2, 3, 5)
                .scan(new LongBinaryOperator() {
                    @Override
                    public long applyAsLong(long value1, long value2) {
                        return value1 / value2;
                    }
                })
                .custom(assertElements(arrayContaining(
                        1800L,
                        1800L / 2,
                        1800L / 2 / 3,
                        1800L / 2 / 3 / 5
                )));
    }

    @Test
    public void testScanOnEmptyStream() {
        LongStream.empty()
                .scan(new LongBinaryOperator() {
                    @Override
                    public long applyAsLong(long left, long right) {
                        return left + right;
                    }
                })
                .custom(assertIsEmpty());
    }

    @Test
    public void testScanWithIdentity() {
        LongStream.of(1, 2, 3)
                .scan(0, new LongBinaryOperator() {
                    @Override
                    public long applyAsLong(long left, long right) {
                        return left + right;
                    }
                })
                .custom(assertElements(arrayContaining(
                        0L, 1L, 3L, 6L
                )));
    }

    @Test
    public void testScanWithIdentityNonAssociative() {
        LongStream.of(2, 3, 5)
                .scan(1800L, new LongBinaryOperator() {
                    @Override
                    public long applyAsLong(long value1, long value2) {
                        return value1 / value2;
                    }
                })
                .custom(assertElements(arrayContaining(
                        1800L,
                        1800L / 2,
                        1800L / 2 / 3,
                        1800L / 2 / 3 / 5
                )));
    }

    @Test
    public void testScanWithIdentityOnEmptyStream() {
        LongStream.empty()
                .scan(9, new LongBinaryOperator() {
                    @Override
                    public long applyAsLong(long left, long right) {
                        return left + right;
                    }
                })
                .custom(assertElements(arrayContaining(
                        9L
                )));
    }
}
