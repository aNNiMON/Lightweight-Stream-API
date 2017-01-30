package com.annimon.stream;

import java.util.NoSuchElementException;
import static org.junit.Assert.*;
import org.junit.Test;

public class LsaExtIteratorTest {

    @Test
    public void testHasNext() {
        assertFalse(new EmptyLsaExtIterator().hasNext());
        assertTrue(new LsaExtIteratorImpl().hasNext());
    }

    @Test(expected = NoSuchElementException.class)
    public void testNext() {
        final LsaExtIteratorImpl iterator = new LsaExtIteratorImpl();
        assertEquals(iterator.next(), "1");
        assertEquals(iterator.next(), "2");

        new EmptyLsaExtIterator().next();
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testRemove() {
        new EmptyLsaExtIterator().remove();
    }

    @Test(expected = NoSuchElementException.class)
    public void testIterator() {
        final LsaExtIteratorImpl iterator = new LsaExtIteratorImpl();
        assertTrue(iterator.hasNext());
        assertTrue(iterator.hasNext());
        assertEquals(iterator.next(), "1");
        assertEquals(iterator.next(), "2");
        assertFalse(iterator.hasNext());
        assertFalse(iterator.hasNext());
        iterator.next();
    }

    public static class EmptyLsaExtIterator extends LsaExtIterator<String> {

        @Override
        public void nextIteration() {
            hasNext = false;
            next = "";
        }
    }

    public static class LsaExtIteratorImpl extends LsaExtIterator<String> {

        int index = 0;

        @Override
        public void nextIteration() {
            hasNext = index < 2;
            next = Integer.toString(++index);
        }
    }

}
