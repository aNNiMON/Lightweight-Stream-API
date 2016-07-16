package com.annimon.stream;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;
import static org.junit.Assert.*;
import org.junit.Test;

public class LazyIteratorTest {

    @Test
    public void testHasNext() {
        assertFalse(newIteratorEmpty().hasNext());
        assertTrue(newIterator().hasNext());
    }

    @Test(expected = NoSuchElementException.class)
    public void testNext() {
        final LazyIterator<String> iterator = newIterator();
        assertEquals(iterator.next(), "1");
        assertEquals(iterator.next(), "2");

        newIteratorEmpty().next();
    }

    @Test
    public void testRemove() {
        final List<String> list = new LinkedList<String>();
        list.addAll(Arrays.asList("1", "2"));

        final LazyIterator<String> iterator = new LazyIterator<String>(list);
        assertEquals(iterator.next(), "1");
        iterator.remove();
        assertTrue(iterator.hasNext());
        assertEquals(iterator.next(), "2");
        iterator.remove();
        assertFalse(iterator.hasNext());

        assertTrue(list.isEmpty());
    }

    @Test(expected = NoSuchElementException.class)
    public void testIterator() {
        final LazyIterator<String> iterator = newIterator();
        assertTrue(iterator.hasNext());
        assertTrue(iterator.hasNext());
        assertEquals(iterator.next(), "1");
        assertEquals(iterator.next(), "2");
        assertFalse(iterator.hasNext());
        assertFalse(iterator.hasNext());
        iterator.next();
    }

    private static LazyIterator<String> newIteratorEmpty() {
        return new LazyIterator<String>(Collections.<String>emptyList());
    }

    private static LazyIterator<String> newIterator() {
        return new LazyIterator<String>(Arrays.asList("1", "2"));
    }
}
