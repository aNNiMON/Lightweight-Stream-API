package com.annimon.stream.intstreamtests;

import com.annimon.stream.Functions;
import com.annimon.stream.IntStream;
import com.annimon.stream.function.IntPredicate;
import com.annimon.stream.function.IntUnaryOperator;
import org.junit.Test;
import static com.annimon.stream.test.hamcrest.IntStreamMatcher.assertElements;
import static org.hamcrest.Matchers.arrayContaining;
import static org.junit.Assert.assertTrue;

public final class FilterTest {

    @Test
    public void testFilter() {
        IntStream.rangeClosed(1, 10)
                .filter(Functions.remainderInt(2))
                .custom(assertElements(arrayContaining(
                        2, 4, 6, 8, 10
                )));

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
