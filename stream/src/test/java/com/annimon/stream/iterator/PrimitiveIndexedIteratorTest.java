package com.annimon.stream.iterator;

import com.annimon.stream.iterator.PrimitiveIterators.*;
import org.junit.Test;
import static com.annimon.stream.test.hamcrest.CommonMatcher.hasOnlyPrivateConstructors;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.closeTo;
import static org.junit.Assert.*;

public class PrimitiveIndexedIteratorTest {

    @Test
    public void testPrivateConstructor() throws Exception {
        assertThat(PrimitiveIndexedIterator.class, hasOnlyPrivateConstructors());
    }

    @Test
    public void testIntDefault() {
        PrimitiveIndexedIterator.OfInt iterator = new PrimitiveIndexedIterator.OfInt(
                new OfInt());

        assertTrue(iterator.hasNext());
        assertThat(iterator.getIndex(), is(0));

        assertThat(iterator.next(), is(1));

        assertTrue(iterator.hasNext());
        assertThat(iterator.getIndex(), is(1));
        assertThat(iterator.getIndex(), is(1));

        assertThat(iterator.next(), is(2));

        assertFalse(iterator.hasNext());
        assertThat(iterator.getIndex(), is(2));
    }

    @Test
    public void testIntWithStartAndStep() {
        PrimitiveIndexedIterator.OfInt iterator = new PrimitiveIndexedIterator.OfInt(
                10, -2, new OfInt());

        assertTrue(iterator.hasNext());
        assertThat(iterator.getIndex(), is(10));

        assertThat(iterator.next(), is(1));

        assertTrue(iterator.hasNext());
        assertThat(iterator.getIndex(), is(8));
        assertThat(iterator.getIndex(), is(8));

        assertThat(iterator.next(), is(2));

        assertFalse(iterator.hasNext());
        assertThat(iterator.getIndex(), is(6));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testIntRemove() {
        PrimitiveIndexedIterator.OfInt iterator = new PrimitiveIndexedIterator.OfInt(new OfInt());
        iterator.next();
        iterator.remove();
    }


    @Test
    public void testLongDefault() {
        PrimitiveIndexedIterator.OfLong iterator = new PrimitiveIndexedIterator.OfLong(new OfLong());

        assertTrue(iterator.hasNext());
        assertThat(iterator.getIndex(), is(0));

        assertThat(iterator.next(), is(1L));

        assertTrue(iterator.hasNext());
        assertThat(iterator.getIndex(), is(1));
        assertThat(iterator.getIndex(), is(1));

        assertThat(iterator.next(), is(2L));

        assertFalse(iterator.hasNext());
        assertThat(iterator.getIndex(), is(2));
    }

    @Test
    public void testLongWithStartAndStep() {
        PrimitiveIndexedIterator.OfLong iterator = new PrimitiveIndexedIterator.OfLong(
                10, -2, new OfLong());

        assertTrue(iterator.hasNext());
        assertThat(iterator.getIndex(), is(10));

        assertThat(iterator.next(), is(1L));

        assertTrue(iterator.hasNext());
        assertThat(iterator.getIndex(), is(8));
        assertThat(iterator.getIndex(), is(8));

        assertThat(iterator.next(), is(2L));

        assertFalse(iterator.hasNext());
        assertThat(iterator.getIndex(), is(6));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testLongRemove() {
        PrimitiveIndexedIterator.OfLong iterator = new PrimitiveIndexedIterator.OfLong(new OfLong());
        iterator.next();
        iterator.remove();
    }


    @Test
    public void testDoubleDefault() {
        PrimitiveIndexedIterator.OfDouble iterator = new PrimitiveIndexedIterator.OfDouble(new OfDouble());

        assertTrue(iterator.hasNext());
        assertThat(iterator.getIndex(), is(0));

        assertThat(iterator.next(), closeTo(1.01, 0.001));

        assertTrue(iterator.hasNext());
        assertThat(iterator.getIndex(), is(1));
        assertThat(iterator.getIndex(), is(1));

        assertThat(iterator.next(), closeTo(2.02, 0.001));

        assertFalse(iterator.hasNext());
        assertThat(iterator.getIndex(), is(2));
    }

    @Test
    public void testDoubleWithStartAndStep() {
        PrimitiveIndexedIterator.OfDouble iterator = new PrimitiveIndexedIterator.OfDouble(
                10, -2, new OfDouble());

        assertTrue(iterator.hasNext());
        assertThat(iterator.getIndex(), is(10));

        assertThat(iterator.next(), closeTo(1.01, 0.001));

        assertTrue(iterator.hasNext());
        assertThat(iterator.getIndex(), is(8));
        assertThat(iterator.getIndex(), is(8));

        assertThat(iterator.next(), closeTo(2.02, 0.001));

        assertFalse(iterator.hasNext());
        assertThat(iterator.getIndex(), is(6));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testDoubleRemove() {
        PrimitiveIndexedIterator.OfDouble iterator = new PrimitiveIndexedIterator.OfDouble(new OfDouble());
        iterator.next();
        iterator.remove();
    }
}
