package com.annimon.stream;

import static org.junit.Assert.*;
import org.junit.Test;

public final class IntPairTest {

    @Test
    public void testGetFirst() {
        final IntPair<String> p = new IntPair<String>(1, "first");
        assertEquals(1, p.getFirst());
    }

    @Test
    public void testGetSecond() {
        final IntPair<String> p = new IntPair<String>(1, "first");
        assertEquals("first", p.getSecond());
    }

    @Test
    public void testEqualsReflexive() {
        final IntPair<String> p = new IntPair<String>(1, "first");
        assertEquals(p, p);
    }

    @Test
    public void testEqualsSymmetric() {
        final IntPair<String> p1 = new IntPair<String>(1, "first");
        final IntPair<String> p2 = new IntPair<String>(1, "first");

        assertEquals(p1, p2);
        assertEquals(p2, p1);
    }

    @Test
    public void testEqualsTransitive() {
        final IntPair<String> p1 = new IntPair<String>(1, "first");
        final IntPair<String> p2 = new IntPair<String>(1, "first");
        final IntPair<String> p3 = new IntPair<String>(1, "first");

        assertEquals(p1, p2);
        assertEquals(p2, p3);
        assertEquals(p1, p3);
    }

    @Test
    public void testEqualsWithNull() {
        final IntPair<String> p = new IntPair<String>(1, "first");
        assertNotEquals(null, p);
    }

    @Test
    public void testEqualsWithDifferentTypes() {
        final IntPair<String> p = new IntPair<String>(1, "first");
        assertFalse(p.equals(1));
    }

    @Test
    public void testEqualsWithDifferentGenericTypes() {
        final IntPair<String> p1 = new IntPair<String>(1, "first");
        final IntPair<Integer> p2 = new IntPair<Integer>(1, 1);

        assertNotEquals(p1, p2);
    }

    @Test
    public void testEqualsWithSwappedValues() {
        final IntPair<Integer> p1 = new IntPair<Integer>(10, 15);
        final IntPair<Integer> p2 = new IntPair<Integer>(15, 10);

        assertNotEquals(p1, p2);
    }

    @Test
    public void testHashCodeWithSameObject() {
        final IntPair<String> p1 = new IntPair<String>(1, "first");
        final IntPair<String> p2 = new IntPair<String>(1, "first");

        int initial = p1.hashCode();
        assertEquals(initial, p1.hashCode());
        assertEquals(initial, p1.hashCode());
        assertEquals(initial, p2.hashCode());
    }

    @Test
    public void testHashCodeWithDifferentGenericType() {
        final IntPair<String> p1 = new IntPair<String>(1, "first");
        final IntPair<Integer> p2 = new IntPair<Integer>(1, 1);

        assertNotEquals(p1.hashCode(), p2.hashCode());
    }

    @Test
    public void testHashCodeWithSwappedValues() {
        final IntPair<Integer> p1 = new IntPair<Integer>(10, 15);
        final IntPair<Integer> p2 = new IntPair<Integer>(15, 10);

        assertNotEquals(p1.hashCode(), p2.hashCode());
    }

    @Test
    public void testHashCodeWithNullSecondValue() {
        final IntPair<String> p1 = new IntPair<String>(0, null);
        final IntPair<String> p2 = new IntPair<String>(0, "first");

        assertNotEquals(p1.hashCode(), p2.hashCode());
    }

    @Test
    public void testToString() {
        final IntPair<String> p1 = new IntPair<String>(1, "first");
        assertEquals("IntPair[1, first]", p1.toString());
    }
}
