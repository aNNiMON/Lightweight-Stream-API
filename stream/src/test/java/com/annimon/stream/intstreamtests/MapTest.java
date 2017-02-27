package com.annimon.stream.intstreamtests;

import com.annimon.stream.IntStream;
import com.annimon.stream.function.IntUnaryOperator;
import org.junit.Test;
import static org.junit.Assert.assertTrue;

public final class MapTest {

    @Test
    public void testMap() {
        assertTrue(IntStream.of(5).map(new IntUnaryOperator() {
            @Override
            public int applyAsInt(int operand) {
                return -operand;
            }
        }).findFirst().getAsInt() == -5);

        assertTrue(IntStream.of(1,2,3,4,5).map(new IntUnaryOperator() {
            @Override
            public int applyAsInt(int operand) {
                return -operand;
            }
        }).sum() < 0);
    }
}
