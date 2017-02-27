package com.annimon.stream.intstreamtests;

import com.annimon.stream.Functions;
import com.annimon.stream.IntStream;
import com.annimon.stream.function.IntPredicate;
import org.junit.Test;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public final class AllMatchTest {

    @Test
    public void testAllMatch() {
        IntStream.empty().allMatch(new IntPredicate() {
            @Override
            public boolean test(int value) {
                throw new IllegalStateException();
            }
        });

        assertTrue(IntStream.of(42).allMatch(new IntPredicate() {
            @Override
            public boolean test(int value) {
                return value == 42;
            }
        }));

        assertFalse(IntStream.of(5, 7, 9, 10, 7, 5).allMatch(
                IntPredicate.Util.negate(Functions.remainderInt(2))));

        assertTrue(IntStream.of(5, 7, 9, 11, 7, 5).allMatch(
                IntPredicate.Util.negate(Functions.remainderInt(2))));
    }
}
