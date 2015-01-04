package com.annimon.stream;

import com.annimon.stream.function.*;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * A sequence of elements supporting aggregate operations.
 * 
 * @author aNNiMON
 * @param <T> the type of the stream elements
 */
public class Stream<T> {
    
    public static <T> Stream<T> of(Iterator<? super T> iterator) {
        return new Stream<T>(iterator);
    }
    
    public static <T> Stream<T> of(Iterable<? super T> iterable) {
        return new Stream<T>(iterable);
    }
    
    public static <T> Stream<T> of(final T... array) {
        return new Stream<T>(new Iterator<T>() {
            
            private int index = 0;

            @Override
            public boolean hasNext() {
                return index < array.length;
            }

            @Override
            public T next() {
                return array[index++];
            }
        });
    }
    
    public static Stream<Integer> ofRange(final int from, final int to) {
        return new Stream<Integer>(new Iterator<Integer>() {
            
            private int index = from;

            @Override
            public boolean hasNext() {
                return index < to;
            }

            @Override
            public Integer next() {
                return index++;
            }
        });
    }
    public static Stream<Long> ofRange(final long from, final long to) {
        return new Stream<Long>(new Iterator<Long>() {
            
            private long index = from;

            @Override
            public boolean hasNext() {
                return index < to;
            }

            @Override
            public Long next() {
                return index++;
            }
        });
    }
    
    
//<editor-fold defaultstate="collapsed" desc="Implementation">
    private final Iterator<? super T> iterator;
    
    private Consumer<? super T> peekAction;
    
    private Stream(Iterator<? super T> iterator) {
        this.iterator = iterator;
    }
    
    private Stream(Iterable<? super T> iterable) {
        this(iterable.iterator());
    }
    
    public Stream<T> filter(final Predicate<? super T> predicate) {
        return new Stream<T>(new Iterator<T>() {
            
            private T next;
            
            @Override
            public boolean hasNext() {
                while (iterator.hasNext()) {
                    next = (T) iterator.next();
                    if (predicate.test(next)) {
                        return true;
                    }
                }
                return false;
            }
            
            @Override
            public T next() {
                return next;
            }
        });
    }
    
    public <R> Stream<R> map(final Function<? super T, ? extends R> mapper) {
        return new Stream<R>(new Iterator<R>() {
            
            @Override
            public boolean hasNext() {
                return iterator.hasNext();
            }
            
            @Override
            public R next() {
                return mapper.apply((T) iterator.next());
            }
        });
    }
    
    public <R> Stream<R> flatMap(final Function<? super T, ? extends Stream<? extends R>> mapper) {
        return new Stream<R>(new Iterator<R>() {
            
            private R next;
            private Iterator<R> inner;
            
            @Override
            public boolean hasNext() {
                if (iterator.hasNext()) {
                    if (inner == null || !inner.hasNext()) {
                        final T arg = (T) iterator.next();
                        final Stream <? extends R> result = mapper.apply(arg);
                        if (result != null) {
                            inner = (Iterator<R>) result.iterator;
                        }
                    }
                }
                if ((inner != null) && inner.hasNext()) {
                    next = inner.next();
                    return true;
                }
                return false;
            }
            
            @Override
            public R next() {
                return next;
            }
        });
    }
    
    public Stream<T> sorted() {
        return sorted(new Comparator<T>() {
            
            @Override
            public int compare(T o1, T o2) {
                Comparable c1 = (Comparable) o1;
                Comparable c2 = (Comparable) o2;
                return c1.compareTo(c2);
            }
        });
    }
    
    public Stream<T> sorted(Comparator<? super T> comparator) {
        final List<T> list = new LinkedList<T>();
        while (iterator.hasNext()) {
            list.add((T) iterator.next());
        }
        Collections.sort(list, comparator);
        return new Stream<T>(list);
    }
    
    public Stream<T> peek(Consumer<? super T> action) {
        this.peekAction = action;
        return this;
    }
    
    public Stream<T> limit(final long maxSize) {
        return new Stream<T>(new Iterator<T>() {
            
            private long index = 0;
            
            @Override
            public boolean hasNext() {
                return (index < maxSize) && iterator.hasNext();
            }
            
            @Override
            public T next() {
                index++;
                return (T) iterator.next();
            }
        });
    }
    
    public Stream<T> skip(long n) {
        for (long i = 0; i < n && iterator.hasNext(); i++) {
            iterator.next();
        }
        return this;
    }
    
    public void forEach(final Consumer<? super T> action) {
        while (iterator.hasNext()) {
            final T value = (T) iterator.next();
            if (peekAction != null) {
                peekAction.accept(value);
            }
            action.accept(value);
        }
    }
    
    public long count() {
        long count = 0;
        while (iterator.hasNext()) {
            if (peekAction != null) {
                final T value = (T) iterator.next();
                peekAction.accept(value);
            } else {
                iterator.next();
            }
            count++;
        }
        return count;
    }
    
//</editor-fold>
}
