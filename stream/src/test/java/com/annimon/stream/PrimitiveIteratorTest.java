package com.annimon.stream;

import java.util.NoSuchElementException;
import static org.junit.Assert.*;
import org.junit.Test;

public class PrimitiveIteratorTest {

    @Test
    public void testOfIntHasNext() {
        assertFalse(new EmptyPrimitiveIteratorOfInt().hasNext());
        assertTrue(new PrimitiveIteratorOfIntImpl().hasNext());
    }

    @Test(expected = NoSuchElementException.class)
    public void testOfIntNext() {
        final PrimitiveIteratorOfIntImpl iterator = new PrimitiveIteratorOfIntImpl();
        assertEquals(iterator.nextInt(), 1);
        assertEquals(iterator.nextInt(), 2);

        new EmptyPrimitiveIteratorOfInt().nextInt();
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testOfIntRemove() {
        new EmptyPrimitiveIteratorOfInt().remove();
    }

    @Test(expected = NoSuchElementException.class)
    public void testOfInt() {
        final PrimitiveIteratorOfIntImpl iterator = new PrimitiveIteratorOfIntImpl();
        assertTrue(iterator.hasNext());
        assertTrue(iterator.hasNext());
        assertEquals(iterator.nextInt(), 1);
        assertEquals(iterator.nextInt(), 2);
        assertFalse(iterator.hasNext());
        assertFalse(iterator.hasNext());
        iterator.nextInt();
    }

    private class EmptyPrimitiveIteratorOfInt extends PrimitiveIterator.OfInt {

        @Override
        public boolean hasNext() {
            return false;
        }

        @Override
        public int nextInt() {
           throw new NoSuchElementException();
        }
    }

    private class PrimitiveIteratorOfIntImpl extends PrimitiveIterator.OfInt {

        private int next = 0;

        @Override
        public boolean hasNext() {
            return next < 2;
        }

        @Override
        public int nextInt() {
            if (!hasNext()) throw new NoSuchElementException();
            return ++next;
        }
    }
}
