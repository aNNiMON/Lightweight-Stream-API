package com.annimon.stream.intstreamtests;

import static com.annimon.stream.test.hamcrest.IntStreamMatcher.assertElements;
import static org.hamcrest.Matchers.arrayContaining;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import com.annimon.stream.IntStream;
import com.annimon.stream.function.IntPredicate;
import com.annimon.stream.function.IntUnaryOperator;
import java.util.concurrent.atomic.AtomicInteger;
import org.junit.Test;

public final class IterateTest {

    @Test
    public void testStreamIterate() {
        IntUnaryOperator operator =
                new IntUnaryOperator() {
                    @Override
                    public int applyAsInt(int operand) {
                        return operand + 1;
                    }
                };

        assertEquals(6, IntStream.iterate(1, operator).limit(3).sum());
        assertTrue(IntStream.iterate(1, operator).iterator().hasNext());
    }

    @SuppressWarnings("ConstantConditions")
    @Test(expected = NullPointerException.class)
    public void testStreamIterateNull() {
        IntStream.iterate(0, null);
    }

    @Test
    public void testStreamIterateWithPredicate() {
        IntPredicate condition =
                new IntPredicate() {
                    @Override
                    public boolean test(int value) {
                        return value < 20;
                    }
                };
        IntUnaryOperator increment =
                new IntUnaryOperator() {
                    @Override
                    public int applyAsInt(int t) {
                        return t + 5;
                    }
                };
        IntStream.iterate(0, condition, increment)
                .custom(assertElements(arrayContaining(0, 5, 10, 15)));
    }

    @Test
    public void testIterateIssue186() {
        final AtomicInteger ai = new AtomicInteger(0);
        int result =
                IntStream.iterate(
                                0,
                                new IntPredicate() {
                                    @Override
                                    public boolean test(int value) {
                                        value = ai.incrementAndGet();
                                        return value < 3;
                                    }
                                },
                                IntUnaryOperator.Util.identity())
                        .findFirst()
                        .orElseThrow();
        assertEquals(0, result);
        assertEquals(1, ai.get());
    }
}
