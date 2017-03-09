package com.annimon.stream.iterator;

import java.util.NoSuchElementException;
import org.junit.Test;
import static com.annimon.stream.test.hamcrest.CommonMatcher.hasOnlyPrivateConstructors;
import static org.hamcrest.Matchers.closeTo;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

public class PrimitiveExtIteratorTest {

    @Test
    public void testPrivateConstructor() throws Exception {
        assertThat(PrimitiveExtIterator.class, hasOnlyPrivateConstructors());
    }


    @Test
    public void testOfIntHasNext() {
        assertFalse(new EmptyPrimitiveExtIteratorOfInt().hasNext());
        assertTrue(new PrimitiveExtIteratorOfIntImpl().hasNext());
    }

    @Test
    public void testOfLongHasNext() {
        assertFalse(new EmptyPrimitiveExtIteratorOfLong().hasNext());
        assertTrue(new PrimitiveExtIteratorOfLongImpl().hasNext());
    }

    @Test
    public void testOfDoubleHasNext() {
        assertFalse(new EmptyPrimitiveExtIteratorOfDouble().hasNext());
        assertTrue(new PrimitiveExtIteratorOfDoubleImpl().hasNext());
    }


    @Test
    public void testOfIntNext() {
        final PrimitiveExtIteratorOfIntImpl iterator = new PrimitiveExtIteratorOfIntImpl();
        assertEquals(iterator.nextInt(), 1);
        assertEquals(iterator.nextInt(), 2);
    }

    @Test(expected = NoSuchElementException.class)
    public void testOfIntNextOnEmptyIterator() {
        new EmptyPrimitiveExtIteratorOfInt().nextInt();
    }

    @Test
    public void testOfLongNext() {
        final PrimitiveExtIteratorOfLongImpl iterator = new PrimitiveExtIteratorOfLongImpl();
        assertEquals(iterator.nextLong(), 1);
        assertEquals(iterator.nextLong(), 2);
    }

    @Test(expected = NoSuchElementException.class)
    public void testOfLongNextOnEmptyIterator() {
        new EmptyPrimitiveExtIteratorOfLong().nextLong();
    }

    @Test
    public void testOfDoubleNext() {
        final PrimitiveExtIteratorOfDoubleImpl iterator = new PrimitiveExtIteratorOfDoubleImpl();
        assertThat(iterator.nextDouble(), closeTo(1.01, 0.00001));
        assertThat(iterator.nextDouble(), closeTo(2.02, 0.00001));
    }

    @Test(expected = NoSuchElementException.class)
    public void testOfDoubleNextOnEmptyIterator() {
        new EmptyPrimitiveExtIteratorOfDouble().nextDouble();
    }


    @Test(expected = UnsupportedOperationException.class)
    public void testOfIntRemove() {
        new EmptyPrimitiveExtIteratorOfInt().remove();
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testOfLongRemove() {
        new EmptyPrimitiveExtIteratorOfLong().remove();
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testOfDoubleRemove() {
        new EmptyPrimitiveExtIteratorOfDouble().remove();
    }


    @Test(expected = NoSuchElementException.class)
    public void testOfInt() {
        final PrimitiveExtIteratorOfIntImpl iterator = new PrimitiveExtIteratorOfIntImpl();
        assertTrue(iterator.hasNext());
        assertTrue(iterator.hasNext());
        assertEquals(iterator.nextInt(), 1);
        assertEquals(iterator.nextInt(), 2);
        assertFalse(iterator.hasNext());
        assertFalse(iterator.hasNext());
        iterator.nextInt();
    }

    @Test(expected = NoSuchElementException.class)
    public void testOfLong() {
        final PrimitiveExtIteratorOfLongImpl iterator = new PrimitiveExtIteratorOfLongImpl();
        assertTrue(iterator.hasNext());
        assertTrue(iterator.hasNext());
        assertEquals(iterator.nextLong(), 1L);
        assertEquals(iterator.nextLong(), 2L);
        assertFalse(iterator.hasNext());
        assertFalse(iterator.hasNext());
        iterator.nextLong();
    }

    @Test(expected = NoSuchElementException.class)
    public void testOfDouble() {
        final PrimitiveExtIteratorOfDoubleImpl iterator = new PrimitiveExtIteratorOfDoubleImpl();
        assertTrue(iterator.hasNext());
        assertTrue(iterator.hasNext());
        assertThat(iterator.nextDouble(), closeTo(1.01, 0.00001));
        assertThat(iterator.nextDouble(), closeTo(2.02, 0.00001));
        assertFalse(iterator.hasNext());
        assertFalse(iterator.hasNext());
        iterator.nextDouble();
    }


    private class EmptyPrimitiveExtIteratorOfInt extends PrimitiveExtIterator.OfInt {

        @Override
        protected void nextIteration() {
            hasNext = false;
        }
    }

    private class PrimitiveExtIteratorOfIntImpl extends PrimitiveExtIterator.OfInt {

        @Override
        protected void nextIteration() {
            hasNext = next < 2;
            next++;
        }
    }

    private class EmptyPrimitiveExtIteratorOfLong extends PrimitiveExtIterator.OfLong {

        @Override
        protected void nextIteration() {
            hasNext = false;
        }
    }

    private class PrimitiveExtIteratorOfLongImpl extends PrimitiveExtIterator.OfLong {

        @Override
        protected void nextIteration() {
            hasNext = next < 2;
            next++;
        }
    }

    private class EmptyPrimitiveExtIteratorOfDouble extends PrimitiveExtIterator.OfDouble {

        @Override
        protected void nextIteration() {
            hasNext = false;
        }
    }

    private class PrimitiveExtIteratorOfDoubleImpl extends PrimitiveExtIterator.OfDouble {

        @Override
        protected void nextIteration() {
            hasNext = next < 2;
            next += 1.01;
        }
    }
}
