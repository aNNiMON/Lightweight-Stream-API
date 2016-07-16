package com.annimon.stream;

import static com.annimon.stream.test.hamcrest.CommonMatcher.hasOnlyPrivateConstructors;
import java.util.NoSuchElementException;
import static org.junit.Assert.*;
import org.junit.Test;

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

    @Test(expected = NoSuchElementException.class)
    public void testOfIntNext() {
        final PrimitiveExtIteratorOfIntImpl infinite = new PrimitiveExtIteratorOfIntImpl();
        assertEquals(infinite.nextInt(), 1);
        assertEquals(infinite.nextInt(), 2);

        new EmptyPrimitiveExtIteratorOfInt().nextInt();
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testOfIntRemove() {
        new EmptyPrimitiveExtIteratorOfInt().remove();
    }

    @Test(expected = NoSuchElementException.class)
    public void testOfInt() {
        final PrimitiveExtIteratorOfIntImpl infinite = new PrimitiveExtIteratorOfIntImpl();
        assertTrue(infinite.hasNext());
        assertTrue(infinite.hasNext());
        assertEquals(infinite.nextInt(), 1);
        assertEquals(infinite.nextInt(), 2);
        assertFalse(infinite.hasNext());
        assertFalse(infinite.hasNext());
        infinite.nextInt();
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
}
