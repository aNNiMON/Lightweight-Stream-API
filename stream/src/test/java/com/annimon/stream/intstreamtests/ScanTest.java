package com.annimon.stream.intstreamtests;

import com.annimon.stream.IntStream;
import com.annimon.stream.function.IntBinaryOperator;
import org.junit.Test;
import static com.annimon.stream.test.hamcrest.IntStreamMatcher.assertElements;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public final class ScanTest {

    @Test
    public void testScan() {
        int[] expected = {1, 3, 6};
        int[] actual = IntStream.of(1, 2, 3)
                .scan(new IntBinaryOperator() {
                    @Override
                    public int applyAsInt(int left, int right) {
                        return left + right;
                    }
                })
                .toArray();
        assertThat(actual, is(expected));
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
                .custom(assertElements(is(new Integer[] {
                        1800,
                        1800 / 2,
                        1800 / 2 / 3,
                        1800 / 2 / 3 / 5
                })));
    }

    @Test
    public void testScanOnEmptyStream() {
        int[] actual = IntStream.empty()
                .scan(new IntBinaryOperator() {
                    @Override
                    public int applyAsInt(int left, int right) {
                        return left + right;
                    }
                })
                .toArray();
        assertThat(actual.length, is(0));
    }

    @Test
    public void testScanWithIdentity() {
        int[] expected = {0, 1, 3, 6};
        int[] actual = IntStream.of(1, 2, 3)
                .scan(0, new IntBinaryOperator() {
                    @Override
                    public int applyAsInt(int left, int right) {
                        return left + right;
                    }
                })
                .toArray();
        assertThat(actual, is(expected));
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
                .custom(assertElements(is(new Integer[] {
                        1800,
                        1800 / 2,
                        1800 / 2 / 3,
                        1800 / 2 / 3 / 5
                })));
    }

    @Test
    public void testScanWithIdentityOnEmptyStream() {
        int[] expected = {9};
        int[] actual = IntStream.empty()
                .scan(9, new IntBinaryOperator() {
                    @Override
                    public int applyAsInt(int left, int right) {
                        return left + right;
                    }
                })
                .toArray();
        assertThat(actual, is(expected));
    }
}
