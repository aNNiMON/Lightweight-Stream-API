package com.annimon.stream.intstreamtests;

import com.annimon.stream.Functions;
import com.annimon.stream.IntStream;
import com.annimon.stream.function.IntPredicate;
import com.annimon.stream.function.IntUnaryOperator;
import org.junit.Test;
import static org.junit.Assert.assertTrue;

public final class FilterTest {

    @Test
    public void testFilter() {
        assertTrue(IntStream.rangeClosed(1, 10).filter(Functions.remainderInt(2)).count() == 5);

        assertTrue(IntStream.rangeClosed(1, 10).filter(Functions.remainderInt(2)).sum() == 30);

        assertTrue(IntStream.iterate(0, new IntUnaryOperator() {
            @Override
            public int applyAsInt(int operand) {
                return operand + 1;
            }
        }).filter(new IntPredicate() {
            @Override
            public boolean test(int value) {
                return value == 0;
            }
        }).findFirst().getAsInt() == 0);
    }
}
