package com.annimon.stream.iterator;

import com.annimon.stream.iterator.PrimitiveIterators.*;
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
        assertFalse(new OfIntEmpty().hasNext());
        assertTrue(new OfInt().hasNext());
    }

    @Test
    public void testOfLongHasNext() {
        assertFalse(new OfLongEmpty().hasNext());
        assertTrue(new OfLong().hasNext());
    }

    @Test
    public void testOfDoubleHasNext() {
        assertFalse(new OfDoubleEmpty().hasNext());
        assertTrue(new OfDouble().hasNext());
    }


    @Test(expected = NoSuchElementException.class)
    public void testOfIntNext() {
        final OfInt iterator = new OfInt();
        assertEquals(iterator.nextInt(), 1);
        assertEquals(iterator.nextInt(), 2);

        new OfIntEmpty().nextInt();
    }

    @Test(expected = NoSuchElementException.class)
    public void testOfLongNext() {
        final OfLong iterator = new OfLong();
        assertEquals(iterator.nextLong(), 1);
        assertEquals(iterator.nextLong(), 2);

        new OfLongEmpty().nextLong();
    }

    @Test(expected = NoSuchElementException.class)
    public void testOfDoubleNext() {
        final OfDouble iterator = new OfDouble();
        assertThat(iterator.nextDouble(), closeTo(1.01, 0.00001));
        assertThat(iterator.nextDouble(), closeTo(2.02, 0.00001));

        new OfDoubleEmpty().nextDouble();
    }


    @Test(expected = UnsupportedOperationException.class)
    public void testOfIntRemove() {
        new OfIntEmpty().remove();
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testOfLongRemove() {
        new OfLongEmpty().remove();
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testOfDoubleRemove() {
        new OfDoubleEmpty().remove();
    }


    @Test(expected = NoSuchElementException.class)
    public void testOfInt() {
        final OfInt iterator = new OfInt();
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
        final OfLong iterator = new OfLong();
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
        final OfDouble iterator = new OfDouble();
        assertTrue(iterator.hasNext());
        assertTrue(iterator.hasNext());
        assertThat(iterator.nextDouble(), closeTo(1.01, 0.00001));
        assertThat(iterator.nextDouble(), closeTo(2.02, 0.00001));
        assertFalse(iterator.hasNext());
        assertFalse(iterator.hasNext());
        iterator.nextDouble();
    }
}
