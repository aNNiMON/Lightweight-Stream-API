package com.annimon.stream.function;

import static com.annimon.stream.test.hamcrest.CommonMatcher.hasOnlyPrivateConstructors;
import static org.junit.Assert.*;
import org.junit.Test;

/**
 * Tests {@code DoublePredicate}.
 */
public class DoublePredicateTest {

    @Test
    public void testLessThanPI() {
        assertTrue(lessThanPI.test(2.01));
        assertFalse(lessThanPI.test(6.0));
    }

    @Test
    public void testIsCloseToPI() {
        assertTrue(isCloseToPI.test(3.14159));
        assertFalse(isCloseToPI.test(3.14));
    }

    @Test
    public void testAndPredicate() {
        DoublePredicate predicate = DoublePredicate.Util.and(lessThanPI, isCloseToPI);

        assertTrue(predicate.test(3.1415));
        assertFalse(predicate.test(3.14));
        assertFalse(predicate.test(3.16));
    }

    @Test
    public void testOrPredicate() {
        DoublePredicate predicate = DoublePredicate.Util.or(lessThanPI, isCloseToPI);

        assertTrue(predicate.test(0));
        assertTrue(predicate.test(3.1415));
        assertTrue(predicate.test(3.1416));
        assertFalse(predicate.test(10.01));
    }

    @Test
    public void testXorPredicate() {
        DoublePredicate predicate = DoublePredicate.Util.xor(lessThanPI, isCloseToPI);

        assertFalse(predicate.test(3.1415));
        assertTrue(predicate.test(2));
        assertTrue(predicate.test(3.1416));
        assertFalse(predicate.test(10.01));
    }

    @Test
    public void testNegatePredicate() {
        DoublePredicate greaterOrEqualsThanPI = DoublePredicate.Util.negate(lessThanPI);

        assertTrue(greaterOrEqualsThanPI.test(Math.PI));
        assertTrue(greaterOrEqualsThanPI.test(8.8005353535));
        assertFalse(greaterOrEqualsThanPI.test(2.11));
    }

    @Test
    public void testPrivateConstructor() throws Exception {
        assertThat(DoublePredicate.Util.class, hasOnlyPrivateConstructors());
    }

    private static final DoublePredicate lessThanPI = new DoublePredicate() {

        @Override
        public boolean test(double value) {
            return value < Math.PI;
        }
    };

    private static final DoublePredicate isCloseToPI = new DoublePredicate() {

        @Override
        public boolean test(double value) {
            return Math.abs(Math.PI - value) < 0.0001;
        }
    };
}
