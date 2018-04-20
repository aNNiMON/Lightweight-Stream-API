package com.annimon.stream.iterator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.junit.Test;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.*;

public class IndexedIteratorTest {

    @Test
    public void testDefault() {
        IndexedIterator<String> iterator = new IndexedIterator<String>(
                Arrays.asList("a", "b", "c").iterator());

        assertTrue(iterator.hasNext());
        assertThat(iterator.getIndex(), is(0));

        String value;
        value = iterator.next();
        assertThat(value, is("a"));

        assertTrue(iterator.hasNext());
        assertThat(iterator.getIndex(), is(1));

        value = iterator.next();
        assertThat(value, is("b"));

        assertTrue(iterator.hasNext());
        assertThat(iterator.getIndex(), is(2));
        assertThat(iterator.getIndex(), is(2));

        value = iterator.next();
        assertThat(value, is("c"));

        assertFalse(iterator.hasNext());
        assertThat(iterator.getIndex(), is(3));
    }

    @Test
    public void testWithStartAndStep() {
        IndexedIterator<String> iterator = new IndexedIterator<String>(
                100, -5, Arrays.asList("a", "b", "c").iterator());

        assertTrue(iterator.hasNext());
        assertThat(iterator.getIndex(), is(100));

        String value;
        value = iterator.next();
        assertThat(value, is("a"));

        assertTrue(iterator.hasNext());
        assertThat(iterator.getIndex(), is(95));

        value = iterator.next();
        assertThat(value, is("b"));

        assertTrue(iterator.hasNext());
        assertThat(iterator.getIndex(), is(90));
        assertThat(iterator.getIndex(), is(90));

        value = iterator.next();
        assertThat(value, is("c"));

        assertFalse(iterator.hasNext());
        assertThat(iterator.getIndex(), is(85));
    }

    @Test
    public void testRemove() {
        List<String> list = new ArrayList<String>(Arrays.asList("a", "b", "c"));
        IndexedIterator<String> iterator = new IndexedIterator<String>(list.iterator());
        iterator.next();
        iterator.next();
        iterator.remove();

        assertThat(list, is(Arrays.asList("a", "c")));
    }
}
