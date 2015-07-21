package com.annimon.stream;

import com.annimon.stream.function.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * A sequence of elements supporting aggregate operations.
 * 
 * @author aNNiMON
 * @param <T> the type of the stream elements
 */
public class Stream<T> {
    
    public static <T> Stream<T> of(final List<? extends T> list) {
        return new Stream<T>(new Iterator<T>() {
            
            private int index = 0;

            @Override
            public boolean hasNext() {
                return index < list.size();
            }

            @Override
            public T next() {
                return list.get(index++);
            }
        });
    }
    
    public static <T> Stream<T> of(Iterator<? extends T> iterator) {
        return new Stream<T>(iterator);
    }
    
    public static <T> Stream<T> of(Iterable<? extends T> iterable) {
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
    private final Iterator<? extends T> iterator;
    
    private Stream(Iterator<? extends T> iterator) {
        this.iterator = iterator;
    }
    
    private Stream(Iterable<? extends T> iterable) {
        this(iterable.iterator());
    }
    
    public Stream<T> filter(final Predicate<? super T> predicate) {
        return new Stream<T>(new Iterator<T>() {
            
            private T next;
            
            @Override
            public boolean hasNext() {
                while (iterator.hasNext()) {
                    next = iterator.next();
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
                return mapper.apply(iterator.next());
            }
        });
    }
    
    public <R> Stream<R> flatMap(final Function<? super T, ? extends Stream<? extends R>> mapper) {
        return new Stream<R>(new Iterator<R>() {
            
            private R next;
            private Iterator<? extends R> inner;
            
            @Override
            public boolean hasNext() {
                if ((inner != null) && inner.hasNext()) {
                    next = inner.next();
                    return true;
                }
                while (iterator.hasNext()) {
                    if (inner == null || !inner.hasNext()) {
                        final T arg = iterator.next();
                        final Stream <? extends R> result = mapper.apply(arg);
                        if (result != null) {
                            inner = result.iterator;
                        }
                    }
                    if ((inner != null) && inner.hasNext()) {
                        next = inner.next();
                        return true;
                    }
                }
                return false;
            }
            
            @Override
            public R next() {
                return next;
            }
        });
    }
    
    public Stream<T> distinct() {
        final Set<T> set = new HashSet<T>();
        while (iterator.hasNext()) {
            set.add(iterator.next());
        }
        return new Stream<T>(set);
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
        final List<T> list = new ArrayList<T>();
        while (iterator.hasNext()) {
            list.add(iterator.next());
        }
        Collections.sort(list, comparator);
        return new Stream<T>(list);
    }
    
    public Stream<T> peek(final Consumer<? super T> action) {
        return new Stream<T>(new Iterator<T>() {
            
            @Override
            public boolean hasNext() {
                return iterator.hasNext();
            }
            
            @Override
            public T next() {
                final T value = iterator.next();
                action.accept(value);
                return value;
            }
        });
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
                return iterator.next();
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
            action.accept(iterator.next());
        }
    }
    
    public T reduce(final T identity, BiFunction<T, T, T> accumulator) {
        T result = identity;
        while (iterator.hasNext()) {
            final T value = iterator.next();
            result = accumulator.apply(result, value);
        }
        return result;
    }
    
    public Optional<T> reduce(BiFunction<T, T, T> accumulator) {
        boolean foundAny = false;
        T result = null;
        while (iterator.hasNext()) {
            final T value = iterator.next();
            if (!foundAny) {
                foundAny = true;
                result = value;
            } else {
                result = accumulator.apply(result, value);
            }
        }
        return foundAny ? Optional.of(result) : (Optional<T>) Optional.empty();
    }
    
    public <R> R collect(Supplier<R> supplier, BiConsumer<R, ? super T> accumulator) {
        R result = supplier.get();
        while (iterator.hasNext()) {
            final T value = iterator.next();
            accumulator.accept(result, value);
        }
        return result;
    }
    
    public <R, A> R collect(Collector<? super T, A, R> collector) {
        A container = collector.supplier().get();
        while (iterator.hasNext()) {
            final T value = iterator.next();
            collector.accumulator().accept(container, value);
        }
        if (collector.finisher() != null)
            return collector.finisher().apply(container);
        return (R) container;
    }
    
    public Optional<T> min(Comparator<? super T> comparator) {
        return reduce(BiFunction.Util.minBy(comparator));
    }
    
    public Optional<T> max(Comparator<? super T> comparator) {
        return reduce(BiFunction.Util.maxBy(comparator));
    }
    
    public long count() {
        long count = 0;
        while (iterator.hasNext()) {
            iterator.next();
            count++;
        }
        return count;
    }
    
    public boolean anyMatch(Predicate<? super T> predicate) {
        return match(predicate, MATCH_ANY);
    }
    
    public boolean allMatch(Predicate<? super T> predicate) {
        return match(predicate, MATCH_ALL);
    }
    
    public boolean noneMatch(Predicate<? super T> predicate) {
        return match(predicate, MATCH_NONE);
    }
    
    public Optional<T> findFirst() {
        if (iterator.hasNext()) {
            return Optional.of(iterator.next());
        }
        return Optional.empty();
    }
    
    private static final int MATCH_ANY = 0;
    private static final int MATCH_ALL = 1;
    private static final int MATCH_NONE = 2;
    
    private boolean match(Predicate<? super T> predicate, int matchKind) {
        final boolean kindAny = (matchKind == MATCH_ANY);
        final boolean kindAll = (matchKind == MATCH_ALL);
        
        while (iterator.hasNext()) {
            final T value = iterator.next();
            
            /*if (predicate.test(value)) {
                // anyMatch -> true
                // noneMatch -> false
                if (!kindAll) {
                    return matchAny;
                }
            } else {
                // allMatch -> false
                if (kindAll) {
                    return false;
                }
            }*/
            // match && !kindAll -> kindAny
            // !match && kindAll -> false
            final boolean match = predicate.test(value);
            if (match ^ kindAll) {
                return kindAny && match; // (match ? kindAny : false);
            }
        }
        // anyMatch -> false
        // allMatch -> true
        // noneMatch -> true
        return !kindAny;
    }
    
//</editor-fold>
}
