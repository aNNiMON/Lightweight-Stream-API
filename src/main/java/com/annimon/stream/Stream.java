package com.annimon.stream;

import com.annimon.stream.function.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * A sequence of elements supporting aggregate operations.
 * 
 * @param <T> the type of the stream elements
 */
public class Stream<T> {
    
    /**
     * Creates a {@code Stream} from {@code List}.
     * 
     * @param <T> the type of the stream elements
     * @param list  the list with elements to be passed to stream
     * @return the new stream
     */
    public static <T> Stream<T> of(final List<? extends T> list) {
        return new Stream<T>(new LsaIterator<T>() {
            
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
    
    /**
     * Creates a {@code Stream} from {@code Map} entries.
     * 
     * @param <K> the type of map keys
     * @param <V> the type of map values
     * @param map  the map with elements to be passed to stream
     * @return the new stream
     */
    public static <K, V> Stream<Map.Entry<K, V>> of(Map<K, V> map) {
        return new Stream<Map.Entry<K, V>>(map.entrySet());
    }
    
    /**
     * Creates a {@code Stream} from any class that implements {@code Iterator} interface.
     * 
     * @param <T> the type of the stream elements
     * @param iterator  the iterator with elements to be passed to stream
     * @return the new stream
     */
    public static <T> Stream<T> of(Iterator<? extends T> iterator) {
        return new Stream<T>(iterator);
    }
    
    /**
     * Creates a {@code Stream} from any class that implements {@code Iterable} interface.
     * 
     * @param <T> the type of the stream elements
     * @param iterable  the iterable with elements to be passed to stream
     * @return the new stream
     */
    public static <T> Stream<T> of(Iterable<? extends T> iterable) {
        return new Stream<T>(iterable);
    }
    
    /**
     * Creates a {@code Stream} from the specified values.
     * 
     * @param <T> the type of the stream elements
     * @param elements  the elements to be passed to stream
     * @return the new stream
     */
    public static <T> Stream<T> of(final T... elements) {
        return new Stream<T>(new LsaIterator<T>() {
            
            private int index = 0;

            @Override
            public boolean hasNext() {
                return index < elements.length;
            }

            @Override
            public T next() {
                return elements[index++];
            }
        });
    }
    
    /**
     * Creates a {@code Stream<Integer>} from not closed range 
     * (from {@code from} inclusive to {@code to} exclusive and incremental step {@code 1}).
     * 
     * @param from  the initial value (inclusive)
     * @param to  the upper bound (exclusive)
     * @return the new stream
     */
    public static Stream<Integer> ofRange(final int from, final int to) {
        return new Stream<Integer>(new LsaIterator<Integer>() {
            
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
    /**
     * Creates a {@code Stream<Long>} from not closed range 
     * (from {@code from} inclusive to {@code to} exclusive and incremental step {@code 1}).
     * 
     * @param from  the initial value (inclusive)
     * @param to  the upper bound (exclusive)
     * @return the new stream
     */
    public static Stream<Long> ofRange(final long from, final long to) {
        return new Stream<Long>(new LsaIterator<Long>() {
            
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
    
    /**
     * Creates a {@code Stream<Integer>} from closed range 
     * (from {@code from} inclusive to {@code to} inclusive and incremental step {@code 1}).
     * 
     * @param from  the initial value (inclusive)
     * @param to  the upper bound (inclusive)
     * @return the new stream
     */
    public static Stream<Integer> ofRangeClosed(final int from, final int to) {
        return new Stream<Integer>(new LsaIterator<Integer>() {
            
            private int index = from;
            private boolean hasNext = (index <= to);

            @Override
            public boolean hasNext() {
                return hasNext;
            }

            @Override
            public Integer next() {
                if (index >= to) {
                    hasNext = false;
                    return to;
                }
                return index++;
            }
        });
    }
    /**
     * Creates a {@code Stream<Long>} from closed range 
     * (from {@code from} inclusive to {@code to} inclusive and incremental step {@code 1}).
     * 
     * @param from  the initial value (inclusive)
     * @param to  the upper bound (inclusive)
     * @return the new stream
     */
    public static Stream<Long> ofRangeClosed(final long from, final long to) {
        return new Stream<Long>(new LsaIterator<Long>() {
            
            private long index = from;
            private boolean hasNext = (index <= to);

            @Override
            public boolean hasNext() {
                return hasNext;
            }

            @Override
            public Long next() {
                if (index >= to) {
                    hasNext = false;
                    return to;
                }
                return index++;
            }
        });
    }
    
    /**
     * Creates a {@code Stream} by elements that generated by {@code Supplier}.
     * 
     * @param <T> the type of the stream elements
     * @param supplier  the {@code Supplier} of generated elements
     * @return the new stream
     */
    public static <T> Stream<T> generate(final Supplier<T> supplier) {
        return new Stream<T>(new LsaIterator<T>() {

            @Override
            public boolean hasNext() {
                return true;
            }

            @Override
            public T next() {
                return supplier.get();
            }
        });
    }
    
    /**
     * Creates a {@code Stream} by applying {@code UnaryOperator} operation to an initial element {@code seed}.
     * 
     * @param <T> the type of the stream elements
     * @param seed  the initial value
     * @param op  operator to produce new element by previous one
     * @return the new stream
     */
    public static <T> Stream<T> iterate(final T seed, final UnaryOperator<T> op) {
        return new Stream<T>(new LsaIterator<T>() {
            
            private boolean firstRun = true;
            private T t;

            @Override
            public boolean hasNext() {
                return true;
            }

            @Override
            public T next() {
                if (firstRun) {
                    firstRun = false;
                    t = seed;
                } else {
                    t = op.apply(t);
                }
                return t;
            }
        });
    }
    
    /**
     * Concatenates two streams.
     * 
     * @param <T> The type of stream elements
     * @param stream1  the first stream
     * @param stream2  the second stream
     * @return the new concatenated stream
     */
    public static <T> Stream<T> concat(Stream<? extends T> stream1, Stream<? extends T> stream2) {
        final Iterator<? extends T> it1 = stream1.iterator;
        final Iterator<? extends T> it2 = stream2.iterator;
        return new Stream<T>(new LsaIterator<T>() {
            
            private T next;
            private boolean hasNext, isInit;
            
            @Override
            public boolean hasNext() {
                if (!isInit) {
                    nextIteration();
                    isInit = true;
                }
                return hasNext;
            }
            
            @Override
            public T next() {
                final T result = next;
                nextIteration();
                return result;
            }
            
            private void nextIteration() {
                if (it1.hasNext()) {
                    next = it1.next();
                    hasNext = true;
                    return;
                }
                if (it2.hasNext()) {
                    next = it2.next();
                    hasNext = true;
                    return;
                }
                hasNext = false;
            }
        });
    } 
    
    
//<editor-fold defaultstate="collapsed" desc="Implementation">
    static final long MAX_ARRAY_SIZE = Integer.MAX_VALUE - 8;
    static final String BAD_SIZE = "Stream size exceeds max array size";

    private final Iterator<? extends T> iterator;
    
    private Stream(Iterator<? extends T> iterator) {
        this.iterator = iterator;
    }

    private Stream(Iterable<? extends T> iterable) {
        this(iterable.iterator());
    }
    
    /**
     * Returns internal stream iterator.
     * 
     * @return internal stream iterator
     */
    public Iterator<? extends T> getIterator() {
        return iterator;
    }
    
    /**
     * Applies custom operator on stream.
     * 
     * Transforming function can return {@code Stream} for intermediate operations, 
     * or any value for terminal operation.
     * 
     * <p>Operator examples:
     * <pre><code>
     *     // Intermediate operator
     *     public static class Reverse&lt;T&gt; implements Function&lt;Stream&lt;T&gt;, Stream&lt;T&gt;&gt; {
     *         &#64;Override
     *         public Stream&lt;T&gt; apply(Stream&lt;T&gt; stream) {
     *             final Iterator&lt;? extends T&gt; iterator = stream.getIterator();
     *             final ArrayDeque&lt;T&gt; deque = new ArrayDeque&lt;T&gt;();
     *             while (iterator.hasNext()) {
     *                 deque.addFirst(iterator.next());
     *             }
     *             return Stream.of(deque.iterator());
     *         }
     *     }
     *     
     *     // Intermediate operator based on existing stream operators
     *     public static class SkipAndLimit&lt;T&gt; implements UnaryOperator&lt;Stream&lt;T&gt;&gt; {
     *         
     *         private final int skip, limit;
     *     
     *         public SkipAndLimit(int skip, int limit) {
     *             this.skip = skip;
     *             this.limit = limit;
     *         }
     *         
     *         &#64;Override
     *         public Stream&lt;T&gt; apply(Stream&lt;T&gt; stream) {
     *             return stream.skip(skip).limit(limit);
     *         }
     *     }
     *     
     *     // Terminal operator
     *     public static class Sum implements Function&lt;Stream&lt;Integer&gt;, Integer&gt; {
     *         &#64;Override
     *         public Integer apply(Stream&lt;Integer&gt; stream) {
     *             return stream.reduce(0, new BinaryOperator&lt;Integer&gt;() {
     *                 &#64;Override
     *                 public Integer apply(Integer value1, Integer value2) {
     *                     return value1 + value2;
     *                 }
     *             });
     *         }
     *     }
     * </code></pre>
     * 
     * @param <R> the type result
     * @param function  a transforming function
     * @return a result of the transforming function
     */
    public <R> R custom(Function<Stream<T>, R> function) {
        return function.apply(this);
    }
    
    /**
     * Returns {@code Stream} with elements that satisfy the given predicate.
     * 
     * <p>This is an intermediate operation.
     * 
     * @param predicate  the predicate used to filter elements
     * @return the new stream
     */
    public Stream<T> filter(final Predicate<? super T> predicate) {
        return new Stream<T>(new LsaIterator<T>() {
            
            private T next;
            private boolean hasNext, isInit;
            
            @Override
            public boolean hasNext() {
                if (!isInit) {
                    nextIteration();
                    isInit = true;
                }
                return hasNext;
            }
            
            @Override
            public T next() {
                final T result = next;
                nextIteration();
                return result;
            }
            
            private void nextIteration() {
                while (iterator.hasNext()) {
                    next = iterator.next();
                    if (predicate.test(next)) {
                        hasNext = true;
                        return;
                    }
                }
                hasNext = false;
            }
        });
    }
    
    /**
     * Returns {@code Stream} with elements that obtained by applying the given function.
     * 
     * <p>This is an intermediate operation.
     * 
     * @param <R> the type of elements in resulting stream
     * @param mapper  the mapper function used to apply to each element
     * @return the new stream
     */
    public <R> Stream<R> map(final Function<? super T, ? extends R> mapper) {
        return new Stream<R>(new LsaIterator<R>() {
            
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
    
    /**
     * Generates {@code Stream} by concatenating elements that obtained by applying the given function.
     * 
     * <p>This is an intermediate operation.
     * 
     * @param <R> the type of elements in resulting stream
     * @param mapper  the mapper function used to apply to each element
     * @return the new stream
     */
    public <R> Stream<R> flatMap(final Function<? super T, ? extends Stream<? extends R>> mapper) {
        return new Stream<R>(new LsaIterator<R>() {
            
            private R next;
            private Iterator<? extends R> inner;
            private boolean hasNext, isInit;
            
            @Override
            public boolean hasNext() {
                if (!isInit) {
                    nextIteration();
                    isInit = true;
                }
                return hasNext;
            }
            
            @Override
            public R next() {
                final R result = next;
                nextIteration();
                return result;
            }
            
            private void nextIteration() {
                if ((inner != null) && inner.hasNext()) {
                    next = inner.next();
                    hasNext = true;
                    return;
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
                        hasNext = true;
                        return;
                    }
                }
                hasNext = false;
            }
        });
    }
    
    /**
     * Returns {@code Stream} with distinct elements (as determinated by {@code equals} method).
     * 
     * <p>This is a stateful intermediate operation.
     * 
     * @return the new stream
     */
    public Stream<T> distinct() {
        final Set<T> set = new HashSet<T>();
        while (iterator.hasNext()) {
            set.add(iterator.next());
        }
        return new Stream<T>(set);
    }
    
    /**
     * Returns {@code Stream} with sorted elements (as determinated by {@code Comparable} interface).
     * 
     * <p>This is a stateful intermediate operation.
     * <p>If the elements of this stream are not {@code Comparable},
     * a {@code java.lang.ClassCastException} may be thrown when the terminal operation is executed.
     * 
     * @return the new stream
     */
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
    
    /**
     * Returns {@code Stream} with sorted elements (as determinated by provided {@code Comparator}).
     * 
     * <p>This is a stateful intermediate operation.
     * 
     * @param comparator  the {@code Comparator} to compare elements
     * @return the new stream
     */
    public Stream<T> sorted(Comparator<? super T> comparator) {
        final List<T> list = new ArrayList<T>();
        while (iterator.hasNext()) {
            list.add(iterator.next());
        }
        Collections.sort(list, comparator);
        return new Stream<T>(list);
    }
    
    /**
     * Returns {@code Stream} with sorted elements (as determinated by {@code Comparable} interface).
     * Each element transformed by given function {@code f} before comparing.
     * 
     * <p>This is a stateful intermediate operation.
     * 
     * @param <R> the type of the result of transforming function
     * @param f  the transformation function
     * @return the new stream
     */
    public <R extends Comparable> Stream<T> sortBy(final Function<? super T, ? extends R> f) {
        return sorted(new Comparator<T>() {
            @Override
            public int compare(T o1, T o2) {
                return f.apply(o1).compareTo(f.apply(o2));
            }
        });
    }
    
    /**
     * Partitions {@code Stream} into {@code Map} entries according to the given classifier function.
     * 
     * <p>This is a stateful intermediate operation.
     * 
     * @param <K> the type of the keys, which are result of the classifier function
     * @param classifier  the classifier function 
     * @return the new stream
     */
    public <K> Stream<Map.Entry<K, List<T>>> groupBy(final Function<? super T, ? extends K> classifier) {
        return Stream.of( collect(Collectors.groupingBy(classifier)) );
    }
    
    /**
     * Perform provided action to each elements.
     * 
     * <p>This is an intermediate operation.
     * 
     * @param action  the action to be performed on each element
     * @return the new stream
     */
    public Stream<T> peek(final Consumer<? super T> action) {
        return new Stream<T>(new LsaIterator<T>() {
            
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
    
    /**
     * Returns {@code Stream} with first {@code maxSize} elements.
     * 
     * <p>This is a short-circuiting stateful intermediate operation.
     * 
     * @param maxSize  the number of elements to limit
     * @return the new stream
     */
    public Stream<T> limit(final long maxSize) {
        return new Stream<T>(new LsaIterator<T>() {
            
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
    
    /**
     * Skips first {@code n} elements and returns {@code Stream} with remaining elements.
     * 
     * <p>This is a stateful intermediate operation.
     * 
     * @param n  the number of elements to skip
     * @return the new stream
     */
    public Stream<T> skip(long n) {
        for (long i = 0; i < n && iterator.hasNext(); i++) {
            iterator.next();
        }
        return this;
    }
    
    /**
     * Performs the given action to each element.
     * 
     * <p>This is a terminal operation.
     * 
     * @param action  the action to be performed on each element
     */
    public void forEach(final Consumer<? super T> action) {
        while (iterator.hasNext()) {
            action.accept(iterator.next());
        }
    }
    
    /**
     * Reduces the elements using provided identity value and the associative accumulation function.
     * 
     * <p>This is a terminal operation.
     * 
     * @param <R> the type of the result
     * @param identity  the initial value
     * @param accumulator  the accumulation function
     * @return the result of the reduction
     */
    public <R> R reduce(R identity, BiFunction<? super R, ? super T, ? extends R> accumulator) {
        R result = identity;
        while (iterator.hasNext()) {
            final T value = iterator.next();
            result = accumulator.apply(result, value);
        }
        return result;
    }
    
    /**
     * Reduces the elements using provided associative accumulation function.
     * 
     * <p>This is a terminal operation.
     * 
     * @param accumulator  the accumulation function
     * @return the result of the reduction
     */
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

    /**
     * Collects elements to an array, the {@code generator} constructor of provided.
     *
     * <p>This is a terminal operation.
     *
     * @param <R> the type of the result
     * @param generator  the array constructor reference that accommodates future array of assigned size
     * @return the result of collect elements
     * @see #collect(com.annimon.stream.Collector)
     * @see #collect(com.annimon.stream.function.Supplier, com.annimon.stream.function.BiConsumer)
     */
    public <R> R[] collect(IntFunction<R[]> generator) {
        Collection<T> container = new ArrayList<T>();
        while (iterator.hasNext()) {
            container.add(iterator.next());
        }
        final int size = container.size();

        if (size >= MAX_ARRAY_SIZE) throw new IllegalArgumentException(BAD_SIZE);

        //noinspection unchecked
        T[] source = container.toArray(Stream.<T>newArray(size));
        R[] boxed = generator.apply(size);

        //noinspection SuspiciousSystemArraycopy
        System.arraycopy(source, 0, boxed, 0, size);
        return boxed;
    }
    
    /**
     * Collects elements to {@code supplier} provided container by applying the given accumulation function.
     * 
     * <p>This is a terminal operation.
     * 
     * @param <R> the type of the result
     * @param supplier  the supplier function that provides container
     * @param accumulator  the accumulation function
     * @return the result of collect elements
     * @see #collect(com.annimon.stream.Collector)
     */
    public <R> R collect(Supplier<R> supplier, BiConsumer<R, ? super T> accumulator) {
        R result = supplier.get();
        while (iterator.hasNext()) {
            final T value = iterator.next();
            accumulator.accept(result, value);
        }
        return result;
    }
    
    /**
     * Collects elements with {@code collector} that encapsulates supplier, accumulator and combiner functions.
     * 
     * <p>This is a terminal operation.
     * 
     * @param <R> the type of result
     * @param <A> the intermediate used by {@code Collector}
     * @param collector  the {@code Collector}
     * @return the result of collect elements
     * @see #collect(com.annimon.stream.function.Supplier, com.annimon.stream.function.BiConsumer) 
     */
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
    
    /**
     * Finds the minimum element according to the given comparator.
     * 
     * <p>This is a terminal operation.
     * 
     * @param comparator  the {@code Comparator} to compare elements
     * @return the minimum element
     */
    public Optional<T> min(Comparator<? super T> comparator) {
        return reduce(BinaryOperator.Util.minBy(comparator));
    }
    
    /**
     * Finds the maximum element according to the given comparator.
     * 
     * <p>This is a terminal operation.
     * 
     * @param comparator  the {@code Comparator} to compare elements
     * @return the maximum element
     */
    public Optional<T> max(Comparator<? super T> comparator) {
        return reduce(BinaryOperator.Util.maxBy(comparator));
    }
    
    /**
     * Counts the number of elements in this stream.
     * 
     * <p>This is a terminal operation.
     * 
     * @return the count of elements
     */
    public long count() {
        long count = 0;
        while (iterator.hasNext()) {
            iterator.next();
            count++;
        }
        return count;
    }
    
    /**
     * Tests whether any elements match the given predicate.
     * 
     * <p>This is a short-circuiting terminal operation.
     * 
     * @param predicate  the predicate used to match elements
     * @return {@code true} if any elements match the given predicate, otherwise {@code false}
     */
    public boolean anyMatch(Predicate<? super T> predicate) {
        return match(predicate, MATCH_ANY);
    }
    
    /**
     * Tests whether all elements match the given predicate.
     * 
     * <p>This is a short-circuiting terminal operation.
     * 
     * @param predicate  the predicate used to match elements
     * @return {@code true} if all elements match the given predicate, otherwise {@code false}
     */
    public boolean allMatch(Predicate<? super T> predicate) {
        return match(predicate, MATCH_ALL);
    }
    
    /**
     * Tests whether no elements match the given predicate.
     * 
     * <p>This is a short-circuiting terminal operation.
     * 
     * @param predicate  the predicate used to match elements
     * @return {@code true} if no elements match the given predicate, otherwise {@code false}
     */
    public boolean noneMatch(Predicate<? super T> predicate) {
        return match(predicate, MATCH_NONE);
    }
    
    /**
     * Returns the first element wrapped by {@code Optional} class.
     * If stream is empty, returns .
     * 
     * <p>This is a short-circuiting terminal operation.
     * 
     * @return an {@code Optional} with first element or {@code Optional.empty()} if stream is empty
     */
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

    @SafeVarargs
    static <E> E[] newArray(int length, E... array) {
        return Arrays.copyOf(array, length);
    }
    
//</editor-fold>
}
