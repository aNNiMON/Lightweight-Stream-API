package com.annimon.stream.intstreamtests;

import com.annimon.stream.Functions;
import com.annimon.stream.IntStream;
import com.annimon.stream.function.IntPredicate;
import org.junit.Test;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public final class NoneMatchTest {

    @Test
    public void testNoneMatch() {
        IntStream.empty().noneMatch(new IntPredicate() {
            @Override
            public boolean test(int value) {
                throw new IllegalStateException();
            }
        });

        assertFalse(IntStream.of(42).noneMatch(new IntPredicate() {
            @Override
            public boolean test(int value) {
                return value == 42;
            }
        }));

        assertFalse(IntStream.of(5, 7, 9, 10, 7, 5).noneMatch(Functions.remainderInt(2)));

        assertTrue(IntStream.of(5, 7, 9, 11, 7, 5).noneMatch(Functions.remainderInt(2)));
    }
}
