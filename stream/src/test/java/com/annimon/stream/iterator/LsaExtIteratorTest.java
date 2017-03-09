package com.annimon.stream.iterator;

import java.util.NoSuchElementException;
import org.junit.Test;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

public class LsaExtIteratorTest {

    @Test
    public void testHasNext() {
        assertFalse(new EmptyLsaExtIterator().hasNext());
        assertTrue(new LsaExtIteratorImpl().hasNext());
    }

    @Test
    public void testNext() {
        final LsaExtIteratorImpl iterator = new LsaExtIteratorImpl();
        assertEquals("1", iterator.next());
        assertEquals("2", iterator.next());
    }

    @Test(expected = NoSuchElementException.class)
    public void testNextOnEmptyIterator() {
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
        assertEquals("1", iterator.next());
        assertEquals("2", iterator.next());
        assertFalse(iterator.hasNext());
        assertFalse(iterator.hasNext());
        iterator.next();
    }

    @Test
    public void testReferenceToPreviousElement() {
        final LsaExtIteratorImpl iterator = new LsaExtIteratorImpl();
        assertEquals(iterator.next(), "1");
        assertEquals(iterator.next(), "2");

        assertThat(iterator.next, is(nullValue()));
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
