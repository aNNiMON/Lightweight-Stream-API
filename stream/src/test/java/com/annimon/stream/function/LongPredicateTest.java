package com.annimon.stream.function;

import com.annimon.stream.Functions;
import java.io.IOException;
import org.junit.Test;
import static com.annimon.stream.test.hamcrest.CommonMatcher.hasOnlyPrivateConstructors;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

/**
 * Tests {@code LongPredicate}.
 */
public class LongPredicateTest {

    @Test
    public void testLessThan100() {
        assertTrue(lessThan100.test(10L));
        assertFalse(lessThan100.test(1000L));
    }

    @Test
    public void testIsEven() {
        assertTrue(isEven.test(10000000054L));
        assertFalse(isEven.test(10000000055L));
    }

    @Test
    public void testAndPredicate() {
        LongPredicate predicate = LongPredicate.Util.and(lessThan100, isEven);

        assertTrue(predicate.test(50));
        assertFalse(predicate.test(55));
        assertFalse(predicate.test(1002));
    }

    @Test
    public void testOrPredicate() {
        LongPredicate predicate = LongPredicate.Util.or(lessThan100, isEven);

        assertTrue(predicate.test(50));
        assertTrue(predicate.test(55));
        assertTrue(predicate.test(1002));
        assertFalse(predicate.test(1001));
    }

    @Test
    public void testXorPredicate() {
        LongPredicate predicate = LongPredicate.Util.xor(lessThan100, isEven);

        assertFalse(predicate.test(50));
        assertTrue(predicate.test(55));
        assertTrue(predicate.test(1002));
        assertFalse(predicate.test(1001));
    }

    @Test
    public void testNegatePredicate() {
        LongPredicate isOdd = LongPredicate.Util.negate(isEven);

        assertTrue(isOdd.test(55));
        assertFalse(isOdd.test(56));
    }
    
    @Test
    public void testSafe() {
        LongPredicate predicate = LongPredicate.Util.safe(new UnsafePredicate());

        assertTrue(predicate.test(40L));
        assertFalse(predicate.test(15L));
        assertFalse(predicate.test(-5L));
    }

    @Test
    public void testSafeWithResultIfFailed() {
        LongPredicate predicate = LongPredicate.Util.safe(new UnsafePredicate(), true);

        assertTrue(predicate.test(40L));
        assertFalse(predicate.test(15L));
        assertTrue(predicate.test(-5L));
    }

    @Test
    public void testPrivateConstructor() throws Exception {
        assertThat(LongPredicate.Util.class, hasOnlyPrivateConstructors());
    }

    private static final LongPredicate isEven = Functions.remainderLong(2);

    private static final LongPredicate lessThan100 = new LongPredicate() {

        @Override
        public boolean test(long value) {
            return value < 100;
        }
    };

    private static class UnsafePredicate implements ThrowableLongPredicate<Throwable> {

        @Override
        public boolean test(long value) throws IOException {
            if (value < 0) {
                throw new IOException();
            }
            return value % 2 == 0;
        }
    }
}
