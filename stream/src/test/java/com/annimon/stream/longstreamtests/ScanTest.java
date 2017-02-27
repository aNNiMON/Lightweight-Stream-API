package com.annimon.stream.longstreamtests;

import com.annimon.stream.LongStream;
import com.annimon.stream.function.LongBinaryOperator;
import org.junit.Test;
import static com.annimon.stream.test.hamcrest.LongStreamMatcher.assertElements;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public final class ScanTest {

    @Test
    public void testScan() {
        long[] expected = {1, 3, 6};
        long[] actual = LongStream.of(1, 2, 3)
                .scan(new LongBinaryOperator() {
                    @Override
                    public long applyAsLong(long left, long right) {
                        return left + right;
                    }
                })
                .toArray();
        assertThat(actual, is(expected));
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
                .custom(assertElements(is(new Long[] {
                        1800L,
                        1800L / 2,
                        1800L / 2 / 3,
                        1800L / 2 / 3 / 5
                })));
    }

    @Test
    public void testScanOnEmptyStream() {
        long[] actual = LongStream.empty()
                .scan(new LongBinaryOperator() {
                    @Override
                    public long applyAsLong(long left, long right) {
                        return left + right;
                    }
                })
                .toArray();
        assertThat(actual.length, is(0));
    }

    @Test
    public void testScanWithIdentity() {
        long[] expected = {0, 1, 3, 6};
        long[] actual = LongStream.of(1, 2, 3)
                .scan(0, new LongBinaryOperator() {
                    @Override
                    public long applyAsLong(long left, long right) {
                        return left + right;
                    }
                })
                .toArray();
        assertThat(actual, is(expected));
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
                .custom(assertElements(is(new Long[] {
                        1800L,
                        1800L / 2,
                        1800L / 2 / 3,
                        1800L / 2 / 3 / 5
                })));
    }

    @Test
    public void testScanWithIdentityOnEmptyStream() {
        long[] expected = {9};
        long[] actual = LongStream.empty()
                .scan(9, new LongBinaryOperator() {
                    @Override
                    public long applyAsLong(long left, long right) {
                        return left + right;
                    }
                })
                .toArray();
        assertThat(actual, is(expected));
    }
}
