package com.annimon.stream.intstreamtests;

import com.annimon.stream.Functions;
import com.annimon.stream.IntStream;
import com.annimon.stream.function.IntPredicate;
import org.junit.Test;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public final class AnyMatchTest {

    @Test
    public void testAnyMatch() {
        IntStream.empty().anyMatch(new IntPredicate() {
            @Override
            public boolean test(int value) {
                throw new IllegalStateException();
            }
        });

        assertTrue(IntStream.of(42).anyMatch(new IntPredicate() {
            @Override
            public boolean test(int value) {
                return value == 42;
            }
        }));

        assertTrue(IntStream.of(5, 7, 9, 10, 7, 5).anyMatch(Functions.remainderInt(2)));

        assertFalse(IntStream.of(5, 7, 9, 11, 7, 5).anyMatch(Functions.remainderInt(2)));
    }
}
