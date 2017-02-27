package com.annimon.stream.longstreamtests;

import com.annimon.stream.CustomOperators;
import com.annimon.stream.LongStream;
import com.annimon.stream.function.LongBinaryOperator;
import org.junit.Test;
import static org.hamcrest.Matchers.closeTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public final class CustomTest {

    @Test(expected = NullPointerException.class)
    public void testCustom() {
        LongStream.empty().custom(null);
    }

    @Test
    public void testCustomLongermediateOperator_Zip() {
        final LongBinaryOperator op = new LongBinaryOperator() {
            @Override
            public long applyAsLong(long left, long right) {
                return left + right;
            }
        };
        LongStream s1 = LongStream.of(1, 3,  5,  7, 9);
        LongStream s2 = LongStream.of(2, 4,  6,  8);
        long[] expected =           {3, 7, 11, 15};
        LongStream result = s1.custom(new CustomOperators.ZipLong(s2, op));
        assertThat(result.toArray(), is(expected));
    }

    @Test
    public void testCustomTerminalOperator_Average() {
        long[] input = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9};
        double average = LongStream.of(input).custom(new CustomOperators.AverageLong());
        assertThat(average, closeTo(4.5, 0.001));
    }
}
