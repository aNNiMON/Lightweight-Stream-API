package com.annimon.stream;

import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import static org.junit.Assert.*;
import org.junit.Test;

/**
 * Standard list/set/map iterator checks write count, so adding data to list
 * after creating Stream can throw ConcurrentModificationException.
 */
public class IteratorIssueTest {

    @Test(expected = ConcurrentModificationException.class)
    public void testArrayListIterator() {
        final int count = 5;
        final List<Integer> data = new ArrayList<Integer>();
        for (int i = 0; i < count; i++) {
            data.add(i);
        }
        Stream stream = Stream.of(data.iterator())
                .filter(Functions.remainder(2));
        for (int i = 0; i < count; i++) {
            data.add(count + i);
        }
        assertEquals(count, stream.count());
    }
    
    @Test
    public void testArrayListIteratorFix() {
        final int count = 5;
        final List<Integer> data = new ArrayList<Integer>(count);
        for (int i = 0; i < count; i++) {
            data.add(i);
        }
        Stream stream = Stream.of(new CustomIterator<Integer>(data))
                .filter(Functions.remainder(2));
        for (int i = 0; i < count; i++) {
            data.add(count + i);
        }
        assertEquals(count, stream.count());
    }
    
    @Test
    public void testHashSetIterator() {
        final int count = 5;
        final Set<Integer> data = new HashSet<Integer>();
        for (int i = 0; i < count; i++) {
            data.add(i);
        }
        Stream stream = Stream.of(data)
                .filter(Functions.remainder(2));
        for (int i = 0; i < count; i++) {
            data.add(count + i);
        }
        assertEquals(count, stream.count());
    }
    
    static class CustomIterator<T> implements Iterator<T> {
        
        private final List<T> list;
        private int index;

        CustomIterator(List<T> list) {
            this.list = list;
            index = 0;
        }
        

        @Override
        public boolean hasNext() {
            return index < list.size();
        }

        @Override
        public T next() {
            return list.get(index++);
        }
        
        @Override
        public void remove() {
            throw new UnsupportedOperationException("remove not supported");
        }
    }
}
