package com.annimon.stream.intstreamtests;

import com.annimon.stream.CustomOperators;
import com.annimon.stream.IntStream;
import com.annimon.stream.function.IntBinaryOperator;
import org.junit.Test;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.closeTo;
import static org.junit.Assert.assertThat;

public final class CustomTest {

    @Test(expected = NullPointerException.class)
    public void testCustomNull() {
        IntStream.empty().custom(null);
    }

    @Test
    public void testCustomIntermediateOperator_Zip() {
        final IntBinaryOperator op = new IntBinaryOperator() {
            @Override
            public int applyAsInt(int left, int right) {
                return left + right;
            }
        };
        IntStream s1 = IntStream.of(1, 3,  5,  7, 9);
        IntStream s2 = IntStream.of(2, 4,  6,  8);
        int[] expected =           {3, 7, 11, 15};
        IntStream result = s1.custom(new CustomOperators.Zip(s2, op));
        assertThat(result.toArray(), is(expected));
    }

    @Test
    public void testCustomTerminalOperator_Average() {
        double average = IntStream.range(0, 10).custom(new CustomOperators.Average());
        assertThat(average, closeTo(4.5, 0.001));
    }
}
