package com.annimon.stream;

import java.util.NoSuchElementException;
import org.junit.Test;
import static com.annimon.stream.test.hamcrest.CommonMatcher.hasOnlyPrivateConstructors;
import static org.hamcrest.Matchers.closeTo;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

public class PrimitiveIteratorTest {

    @Test
    public void testPrivateConstructor() throws Exception {
        assertThat(PrimitiveIterator.class, hasOnlyPrivateConstructors());
    }

    @Test
    public void testOfIntHasNext() {
        assertFalse(new EmptyPrimitiveIteratorOfInt().hasNext());
        assertTrue(new PrimitiveIteratorOfIntImpl().hasNext());
    }

    @Test
    public void testOfDoubleHasNext() {
        assertFalse(new EmptyPrimitiveIteratorOfDouble().hasNext());
        assertTrue(new PrimitiveIteratorOfDoubleImpl().hasNext());
    }


    @Test(expected = NoSuchElementException.class)
    public void testOfIntNext() {
        final PrimitiveIteratorOfIntImpl iterator = new PrimitiveIteratorOfIntImpl();
        assertEquals(iterator.nextInt(), 1);
        assertEquals(iterator.nextInt(), 2);

        new EmptyPrimitiveIteratorOfInt().nextInt();
    }

    @Test(expected = NoSuchElementException.class)
    public void testOfDoubleNext() {
        final PrimitiveIteratorOfDoubleImpl iterator = new PrimitiveIteratorOfDoubleImpl();
        assertThat(iterator.nextDouble(), closeTo(1.01, 0.00001));
        assertThat(iterator.nextDouble(), closeTo(2.02, 0.00001));

        new EmptyPrimitiveIteratorOfDouble().nextDouble();
    }


    @Test(expected = UnsupportedOperationException.class)
    public void testOfIntRemove() {
        new EmptyPrimitiveIteratorOfInt().remove();
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testOfDoubleRemove() {
        new EmptyPrimitiveIteratorOfDouble().remove();
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

    @Test(expected = NoSuchElementException.class)
    public void testOfDouble() {
        final PrimitiveIteratorOfDoubleImpl iterator = new PrimitiveIteratorOfDoubleImpl();
        assertTrue(iterator.hasNext());
        assertTrue(iterator.hasNext());
        assertThat(iterator.nextDouble(), closeTo(1.01, 0.00001));
        assertThat(iterator.nextDouble(), closeTo(2.02, 0.00001));
        assertFalse(iterator.hasNext());
        assertFalse(iterator.hasNext());
        iterator.nextDouble();
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

    private class EmptyPrimitiveIteratorOfDouble extends PrimitiveIterator.OfDouble {

        @Override
        public boolean hasNext() {
            return false;
        }

        @Override
        public double nextDouble() {
           throw new NoSuchElementException();
        }
    }

    private class PrimitiveIteratorOfDoubleImpl extends PrimitiveIterator.OfDouble {

        private double next = 0;

        @Override
        public boolean hasNext() {
            return next < 2;
        }

        @Override
        public double nextDouble() {
            if (!hasNext()) throw new NoSuchElementException();
            next += 1.01;
            return next;
        }
    }
}
