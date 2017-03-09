package com.annimon.stream.iterator;

import java.util.NoSuchElementException;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class LsaIteratorTest {

    @Test
    public void testHasNext() {
        assertFalse(new EmptyLsaIterator().hasNext());
        assertTrue(new LsaIteratorImpl().hasNext());
    }

    @Test(expected = NoSuchElementException.class)
    public void testNext() {
        final LsaIteratorImpl iterator = new LsaIteratorImpl();
        assertEquals(iterator.next(), "1");
        assertEquals(iterator.next(), "2");

        new EmptyLsaIterator().next();
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testRemove() {
        new EmptyLsaIterator().remove();
    }

    @Test(expected = NoSuchElementException.class)
    public void testIterator() {
        final LsaIteratorImpl iterator = new LsaIteratorImpl();
        assertTrue(iterator.hasNext());
        assertTrue(iterator.hasNext());
        assertEquals(iterator.next(), "1");
        assertEquals(iterator.next(), "2");
        assertFalse(iterator.hasNext());
        assertFalse(iterator.hasNext());
        iterator.next();
    }

    public static class EmptyLsaIterator extends LsaIterator<String> {

        @Override
        public boolean hasNext() {
            return false;
        }

        @Override
        public String nextIteration() {
            return "";
        }
    }

    public static class LsaIteratorImpl extends LsaIterator<String> {

        int index = 0;

        @Override
        public boolean hasNext() {
            return index < 2;
        }

        @Override
        public String nextIteration() {
            return Integer.toString(++index);
        }
    }

}
