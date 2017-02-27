package com.annimon.stream.intstreamtests;

import com.annimon.stream.IntStream;
import com.annimon.stream.function.IntBinaryOperator;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

public final class ReduceTest {

    @Test
    public void testReduceIdentity() {
        assertEquals(IntStream.empty().reduce(1, new IntBinaryOperator() {
            @Override
            public int applyAsInt(int left, int right) {
                return left + right;
            }
        }), 1);

        assertEquals(IntStream.of(42).reduce(1, new IntBinaryOperator() {
            @Override
            public int applyAsInt(int left, int right) {
                return left + right;
            }
        }), 43);

        assertEquals(IntStream.of(5, 7, 3, 9, 1).reduce(Integer.MIN_VALUE, new IntBinaryOperator() {
            @Override
            public int applyAsInt(int left, int right) {
                if (left >= right)
                    return left;

                return right;
            }
        }), 9);
    }

    @Test
    public void testReduce() {
        assertFalse(IntStream.empty().reduce(new IntBinaryOperator() {
            @Override
            public int applyAsInt(int left, int right) {
                throw new IllegalStateException();
            }
        }).isPresent());

        assertEquals(IntStream.of(42).reduce(new IntBinaryOperator() {
            @Override
            public int applyAsInt(int left, int right) {
                throw new IllegalStateException();
            }
        }).getAsInt(), 42);

        assertEquals(IntStream.of(41, 42).reduce(new IntBinaryOperator() {
            @Override
            public int applyAsInt(int left, int right) {
                if (right > left)
                    return right;
                return left;
            }
        }).getAsInt(), 42);
    }
}
