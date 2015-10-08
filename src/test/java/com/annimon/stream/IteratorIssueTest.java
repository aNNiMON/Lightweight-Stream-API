package com.annimon.stream;

import com.annimon.stream.function.Predicate;
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
 * 
 * @author aNNiMON
 */
public class IteratorIssueTest {

    @Test(expected = ConcurrentModificationException.class)
    public void arrayListIterator() {
        final int count = 5;
        final List<Integer> data = new ArrayList<Integer>();
        for (int i = 0; i < count; i++) {
            data.add(i);
        }
        Stream stream = Stream.of(data.iterator()).filter(new Predicate<Integer>() {

            @Override
            public boolean test(Integer value) {
                return value % 2 == 0;
            }
        });
        for (int i = 0; i < count; i++) {
            data.add(count + i);
        }
        assertEquals(count, stream.count());
    }
    
    @Test
    public void arrayListIteratorFix() {
        final int count = 5;
        final List<Integer> data = new ArrayList<Integer>();
        for (int i = 0; i < count; i++) {
            data.add(i);
        }
        Stream stream = Stream.of(new CustomIterator<Integer>(data)).filter(new Predicate<Integer>() {

            @Override
            public boolean test(Integer value) {
                return value % 2 == 0;
            }
        });
        for (int i = 0; i < count; i++) {
            data.add(count + i);
        }
        assertEquals(count, stream.count());
    }
    
    @Test(expected = ConcurrentModificationException.class)
    public void hashSetIterator() {
        final int count = 5;
        final Set<Integer> data = new HashSet<Integer>();
        for (int i = 0; i < count; i++) {
            data.add(i);
        }
        Stream stream = Stream.of(data).filter(new Predicate<Integer>() {

            @Override
            public boolean test(Integer value) {
                return value % 2 == 0;
            }
        });
        for (int i = 0; i < count; i++) {
            data.add(count + i);
        }
        assertEquals(count, stream.count());
    }
    
    class CustomIterator<T> implements Iterator<T> {
        
        private final List<T> list;
        private int index;

        public CustomIterator(List<T> list) {
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
