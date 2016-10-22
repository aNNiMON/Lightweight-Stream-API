package com.annimon.stream;

import com.annimon.stream.function.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Queue;
import java.util.Set;

/**
 * A sequence of elements supporting aggregate operations.
 *
 * @param <T> the type of the stream elements
 */
public class Stream<T> {

    /**
     * Returns an empty stream.
     *
     * @param <T> the type of the stream elements
     * @return the new empty stream
     */
    public static <T> Stream<T> empty() {
        return of(Collections.<T>emptyList());
    }

    /**
     * Creates a {@code Stream} from {@code Map} entries.
     *
     * @param <K> the type of map keys
     * @param <V> the type of map values
     * @param map  the map with elements to be passed to stream
     * @return the new stream
     * @throws NullPointerException if {@code map} is null
     */
    public static <K, V> Stream<Map.Entry<K, V>> of(Map<K, V> map) {
        Objects.requireNonNull(map);
        return new Stream<Map.Entry<K, V>>(map.entrySet());
    }

    /**
     * Creates a {@code Stream} from any class that implements {@code Iterator} interface.
     *
     * @param <T> the type of the stream elements
     * @param iterator  the iterator with elements to be passed to stream
     * @return the new stream
     * @throws NullPointerException if {@code iterator} is null
     */
    public static <T> Stream<T> of(Iterator<? extends T> iterator) {
        Objects.requireNonNull(iterator);
        return new Stream<T>(iterator);
    }

    /**
     * Creates a {@code Stream} from any class that implements {@code Iterable} interface.
     *
     * @param <T> the type of the stream elements
     * @param iterable  the {@code Iterable} with elements to be passed to stream
     * @return the new stream
     * @throws NullPointerException if {@code iterable} is null
     */
    public static <T> Stream<T> of(Iterable<? extends T> iterable) {
        Objects.requireNonNull(iterable);
        return new Stream<T>(iterable);
    }

    /**
     * Creates a {@code Stream} from the specified values.
     *
     * @param <T> the type of the stream elements
     * @param elements  the elements to be passed to stream
     * @return the new stream
     * @throws NullPointerException if {@code elements} is null
     */
    public static <T> Stream<T> of(final T... elements) {
        Objects.requireNonNull(elements);
        return new Stream<T>(new LsaIterator<T>() {

            private int index = 0;

            @Override
            public boolean hasNext() {
                return index < elements.length;
            }

            @Override
            public T nextIteration() {
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
    public static Stream<Integer> range(final int from, final int to) {
        return new Stream<Integer>(new LsaIterator<Integer>() {

            private int index = from;

            @Override
            public boolean hasNext() {
                return index < to;
            }

            @Override
            public Integer nextIteration() {
                return index++;
            }
        });
    }

    /**
     * Creates a {@code Stream<Integer>} from not closed range
     * (from {@code from} inclusive to {@code to} exclusive and incremental step {@code 1}).
     *
     * @param from  the initial value (inclusive)
     * @param to  the upper bound (exclusive)
     * @deprecated  As of release 1.0.7, replaced by {@link #range(int, int)}
     * @return the new stream
     */
    @Deprecated
    public static Stream<Integer> ofRange(final int from, final int to) {
        return range(from, to);
    }

    /**
     * Creates a {@code Stream<Long>} from not closed range
     * (from {@code from} inclusive to {@code to} exclusive and incremental step {@code 1}).
     *
     * @param from  the initial value (inclusive)
     * @param to  the upper bound (exclusive)
     * @return the new stream
     */
    public static Stream<Long> range(final long from, final long to) {
        return new Stream<Long>(new LsaIterator<Long>() {

            private long index = from;

            @Override
            public boolean hasNext() {
                return index < to;
            }

            @Override
            public Long nextIteration() {
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
     * @deprecated  As of release 1.0.7, replaced by {@link #range(long, long)}
     * @return the new stream
     */
    @Deprecated
    public static Stream<Long> ofRange(final long from, final long to) {
        return range(from, to);
    }

    /**
     * Creates a {@code Stream<Integer>} from closed range
     * (from {@code from} inclusive to {@code to} inclusive and incremental step {@code 1}).
     *
     * @param from  the initial value (inclusive)
     * @param to  the upper bound (inclusive)
     * @return the new stream
     */
    public static Stream<Integer> rangeClosed(final int from, final int to) {
        return new Stream<Integer>(new LsaIterator<Integer>() {

            private int index = from;
            private boolean hasNext = (index <= to);

            @Override
            public boolean hasNext() {
                return hasNext;
            }

            @Override
            public Integer nextIteration() {
                if (index >= to) {
                    hasNext = false;
                    return to;
                }
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
     * @deprecated  As of release 1.0.7, replaced by {@link #rangeClosed(int, int)}
     * @return the new stream
     */
    @Deprecated
    public static Stream<Integer> ofRangeClosed(final int from, final int to) {
        return rangeClosed(from, to);
    }

    /**
     * Creates a {@code Stream<Long>} from closed range
     * (from {@code from} inclusive to {@code to} inclusive and incremental step {@code 1}).
     *
     * @param from  the initial value (inclusive)
     * @param to  the upper bound (inclusive)
     * @return the new stream
     */
    public static Stream<Long> rangeClosed(final long from, final long to) {
        return new Stream<Long>(new LsaIterator<Long>() {

            private long index = from;
            private boolean hasNext = (index <= to);

            @Override
            public boolean hasNext() {
                return hasNext;
            }

            @Override
            public Long nextIteration() {
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
     * @deprecated  As of release 1.0.7, replaced by {@link #rangeClosed(long, long)}
     * @return the new stream
     */
    @Deprecated
    public static Stream<Long> ofRangeClosed(final long from, final long to) {
        return rangeClosed(from, to);
    }

    /**
     * Creates a {@code Stream} by elements that generated by {@code Supplier}.
     *
     * @param <T> the type of the stream elements
     * @param supplier  the {@code Supplier} of generated elements
     * @return the new stream
     * @throws NullPointerException if {@code supplier} is null
     */
    public static <T> Stream<T> generate(final Supplier<T> supplier) {
        Objects.requireNonNull(supplier);
        return new Stream<T>(new LsaIterator<T>() {

            @Override
            public boolean hasNext() {
                return true;
            }

            @Override
            public T nextIteration() {
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
     * @throws NullPointerException if {@code op} is null
     */
    public static <T> Stream<T> iterate(final T seed, final UnaryOperator<T> op) {
        Objects.requireNonNull(op);
        return new Stream<T>(new LsaIterator<T>() {

            private boolean firstRun = true;
            private T t;

            @Override
            public boolean hasNext() {
                return true;
            }

            @Override
            public T nextIteration() {
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
     * @throws NullPointerException if {@code stream1} or {@code stream2} is null
     */
    public static <T> Stream<T> concat(Stream<? extends T> stream1, Stream<? extends T> stream2) {
        Objects.requireNonNull(stream1);
        Objects.requireNonNull(stream2);
        final Iterator<? extends T> it1 = stream1.iterator;
        final Iterator<? extends T> it2 = stream2.iterator;
        return new Stream<T>(new LsaExtIterator<T>() {

            @Override
            protected void nextIteration() {
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

    /**
     * Combines two streams by applying specified combiner function to each element at same position.
     *
     * @param <F> the type of first stream elements
     * @param <S> the type of second stream elements
     * @param <R> the type of elements in resulting stream
     * @param stream1  the first stream
     * @param stream2  the second stream
     * @param combiner  the combiner function used to apply to each element
     * @return the new stream
     * @throws NullPointerException if {@code stream1} or {@code stream2} is null
     */
    public static <F, S, R> Stream<R> zip(Stream<? extends F> stream1, Stream<? extends S> stream2,
            final BiFunction<? super F, ? super S, ? extends R> combiner) {
        Objects.requireNonNull(stream1);
        Objects.requireNonNull(stream2);
        return zip(stream1.iterator, stream2.iterator, combiner);
    }

    /**
     * Combines two iterators to a stream by applying specified combiner function to each element at same position.
     *
     * @param <F> the type of first iterator elements
     * @param <S> the type of second iterator elements
     * @param <R> the type of elements in resulting stream
     * @param iterator1  the first iterator
     * @param iterator2  the second iterator
     * @param combiner  the combiner function used to apply to each element
     * @return the new stream
     * @throws NullPointerException if {@code iterator1} or {@code iterator2} is null
     * @since 1.1.2
     */
    public static <F, S, R> Stream<R> zip(final Iterator<? extends F> iterator1,
            final Iterator<? extends S> iterator2,
            final BiFunction<? super F, ? super S, ? extends R> combiner) {
        Objects.requireNonNull(iterator1);
        Objects.requireNonNull(iterator2);
        return new Stream<R>(new LsaIterator<R>() {
            @Override
            public boolean hasNext() {
                return iterator1.hasNext() && iterator2.hasNext();
            }

            @Override
            public R nextIteration() {
                return combiner.apply(iterator1.next(), iterator2.next());
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
        this(new LazyIterator<T>(iterable));
    }

    /**
     * Returns internal stream iterator.
     *
     * @deprecated  As of release 1.1.1, replaced by {@link #iterator()}
     * @return internal stream iterator
     */
    @Deprecated
    public Iterator<? extends T> getIterator() {
        return iterator;
    }

    /**
     * Returns internal stream iterator.
     *
     * @return internal stream iterator
     */
    public Iterator<? extends T> iterator() {
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
     *     public class Reverse&lt;T&gt; implements Function&lt;Stream&lt;T&gt;, Stream&lt;T&gt;&gt; {
     *         &#64;Override
     *         public Stream&lt;T&gt; apply(Stream&lt;T&gt; stream) {
     *             final Iterator&lt;? extends T&gt; iterator = stream.iterator();
     *             final ArrayDeque&lt;T&gt; deque = new ArrayDeque&lt;T&gt;();
     *             while (iterator.hasNext()) {
     *                 deque.addFirst(iterator.next());
     *             }
     *             return Stream.of(deque.iterator());
     *         }
     *     }
     *
     *     // Intermediate operator based on existing stream operators
     *     public class SkipAndLimit&lt;T&gt; implements UnaryOperator&lt;Stream&lt;T&gt;&gt; {
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
     *     public class Sum implements Function&lt;Stream&lt;Integer&gt;, Integer&gt; {
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
     * @param <R> the type of the result
     * @param function  a transforming function
     * @return a result of the transforming function
     * @throws NullPointerException if {@code function} is null
     */
    public <R> R custom(Function<Stream<T>, R> function) {
        Objects.requireNonNull(function);
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
        return new Stream<T>(new Iterator<T>() {

            private boolean hasNext, hasNextEvaluated;
            private T next;

            @Override
            public boolean hasNext() {
                if (!hasNextEvaluated) {
                    nextIteration();
                    hasNextEvaluated = true;
                }
                return hasNext;
            }

            @Override
            public T next() {
                if (!hasNextEvaluated) {
                    hasNext = hasNext();
                }
                if (!hasNext) {
                    throw new NoSuchElementException();
                }
                hasNextEvaluated = false;
                return next;
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

            @Override
            public void remove() {
                throw new UnsupportedOperationException("remove not supported");
            }
        });
    }

    /**
     * Returns {@code Stream} with elements that does not satisfy the given predicate.
     *
     * <p>This is an intermediate operation.
     *
     * @param predicate  the predicate used to filter elements
     * @return the new stream
     */
    public Stream<T> filterNot(final Predicate<? super T> predicate) {
        return filter(Predicate.Util.negate(predicate));
    }

    /**
     * Returns a stream consisting of the elements of this stream which are
     * instances of given class.
     *
     * <p>This is an intermediate operation.
     *
     * @param <TT> a type of instances to select.
     * @param clazz a class which instances should be selected
     * @return the new stream of type passed as param
     */
    @SuppressWarnings("unchecked")
    public <TT> Stream<TT> select(final Class<TT> clazz) {
        return (Stream<TT>) filter(new Predicate<T>() {
            @Override
            public boolean test(T value) {
                return clazz.isInstance(value);
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
            public R nextIteration() {
                return mapper.apply(iterator.next());
            }
        });
    }

    /**
     * Returns {@code IntStream} with elements that obtained by applying the given function.
     *
     * <p>This is an intermediate operation.
     *
     * @param mapper  the mapper function used to apply to each element
     * @return the new {@code IntStream}
     * @see #map(com.annimon.stream.function.Function)
     */
    public IntStream mapToInt(final ToIntFunction<? super T> mapper) {
        return new IntStream(new PrimitiveIterator.OfInt() {

            @Override
            public boolean hasNext() {
                return iterator.hasNext();
            }

            @Override
            public int nextInt() {
                return mapper.applyAsInt(iterator.next());
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
        return new Stream<R>(new LsaExtIterator<R>() {

            private Iterator<? extends R> inner;

            @Override
            protected void nextIteration() {
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
     * Generates {@code IntStream} by concatenating elements that obtained by applying the given function.
     *
     * <p>This is an intermediate operation.
     *
     * @param mapper  the mapper function used to apply to each element
     * @return the new {@code IntStream}
     * @see #flatMap(com.annimon.stream.function.Function)
     */
    public IntStream flatMapToInt(final Function<? super T, ? extends IntStream> mapper) {
        return new IntStream(new PrimitiveExtIterator.OfInt() {

            private PrimitiveIterator.OfInt inner;

            @Override
            protected void nextIteration() {
                if ((inner != null) && inner.hasNext()) {
                    next = inner.next();
                    hasNext = true;
                    return;
                }
                while (iterator.hasNext()) {
                    if (inner == null || !inner.hasNext()) {
                        final T arg = iterator.next();
                        final IntStream result = mapper.apply(arg);
                        if (result != null) {
                            inner = result.iterator();
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
     * Returns {@code Stream} with indexed elements.
     * Indexing starts from 0 with step 1.
     *
     * <p>This is an intermediate operation.
     *
     * @return the new {@code IntPair} stream
     * @since 1.1.2
     */
    public Stream<IntPair<T>> indexed() {
        return indexed(0, 1);
    }

    /**
     * Returns {@code Stream} with indexed elements.
     *
     * <p>This is an intermediate operation.
     *
     * @param from  the initial value (inclusive)
     * @param step  the step
     * @return the new {@code IntPair} stream
     * @since 1.1.2
     */
    public Stream<IntPair<T>> indexed(final int from, final int step) {
        return map(new Function<T, IntPair<T>>() {

            private int index = from - step;

            @Override
            public IntPair<T> apply(T value) {
                return new IntPair<T>(index += step, value);
            }
        });
    }

    /**
     * Returns {@code Stream} with distinct elements (as determinated by {@code hashCode} method).
     *
     * <p>This is a stateful intermediate operation.
     *
     * @return the new stream
     */
    public Stream<T> distinct() {
        return new Stream<T>(new LsaExtIterator<T>() {

            private Iterator<T> distinctIterator;

            @Override
            protected void nextIteration() {
                if (!isInit) {
                    final Set<T> set = new LinkedHashSet<T>();
                    while (iterator.hasNext()) {
                        set.add(iterator.next());
                    }
                    distinctIterator = set.iterator();
                }
                hasNext = distinctIterator.hasNext();
                if (hasNext) {
                    next = distinctIterator.next();
                }
            }
        });
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

            @SuppressWarnings("unchecked")
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
    public Stream<T> sorted(final Comparator<? super T> comparator) {
        return new Stream<T>(new LsaExtIterator<T>() {

            private Iterator<T> sortedIterator;

            @Override
            protected void nextIteration() {
                if (!isInit) {
                    final List<T> list = collectToList();
                    Collections.sort(list, comparator);
                    sortedIterator = list.iterator();
                }
                hasNext = sortedIterator.hasNext();
                if (hasNext) {
                    next = sortedIterator.next();
                }
            }
        });
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
    public <R extends Comparable<? super R>> Stream<T> sortBy(final Function<? super T, ? extends R> f) {
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
        return Stream.of( collect(Collectors.<T, K>groupingBy(classifier)) );
    }

    /**
     * Partitions {@code Stream} into {@code List}s according to the given classifier function. In contrast
     * to {@link #groupBy(Function)}, this method assumes that the elements of the stream are sorted.
     * Because of this assumption, it does not need to first collect all elements and then partition them.
     * Instead, it can emit a {@code List} of elements when it reaches the first element that does not
     * belong to the same chunk as the previous elements.
     *
     * <p>This is an intermediate operation.
     *
     * @param <K> the type of the keys, which are the result of the classifier function
     * @param classifier  the classifier function
     * @return the new stream
     */
    public <K> Stream<List<T>> chunkBy(final Function<? super T, ? extends K> classifier) {
        return new Stream<List<T>>(new LsaIterator<List<T>>() {
            private T next;
            private boolean peekedNext;

            @Override
            public boolean hasNext() {
                return peekedNext || iterator.hasNext();
            }

            @Override
            public List<T> nextIteration() {
                K key = classifier.apply(peek());

                List<T> list = new ArrayList<T>();
                do {
                    list.add(takeNext());
                }
                while (iterator.hasNext() && key.equals(classifier.apply(peek())));

                return list;
            }

            private T takeNext() {
                T element = peek();
                peekedNext = false;
                return element;
            }

            private T peek() {
                if (!peekedNext) {
                    next = iterator.next();
                    peekedNext = true;
                }
                return next;
            }
        });
    }

    /**
     * Samples the {@code Stream} by emitting every n-th element.
     *
     * <p>This is an intermediate operation.
     *
     * @param stepWidth  step width
     * @return the new stream
     * @throws IllegalArgumentException if {@code stepWidth} is zero or negative
     */
    public Stream<T> sample(final int stepWidth) {
        if (stepWidth <= 0) throw new IllegalArgumentException("stepWidth cannot be zero or negative");
        if (stepWidth == 1) return this;
        return slidingWindow(1, stepWidth).map(new Function<List<T>, T>() {
            @Override
            public T apply(List<T> list) {
                return list.get(0);
            }
        });
    }

    /**
     * Partitions {@code Stream} into {@code List}s of fixed size by sliding over the elements of the stream.
     * It starts with the first element and in each iteration moves by 1. This method yields the same results
     * as calling {@link #slidingWindow(int, int)} with a {@code stepWidth} of 1.
     *
     * <p>This is an intermediate operation.
     *
     * @param windowSize  number of elements that will be emitted together in a list
     * @return the new stream
     * @see #slidingWindow(int, int)
     */
    public Stream<List<T>> slidingWindow(final int windowSize) {
        return slidingWindow(windowSize, 1);
    }

    /**
     * Partitions {@code Stream} into {@code List}s of fixed size by sliding over the elements of the stream.
     * It starts with the first element and in each iteration moves by the given step width. This method
     * allows, for example, to partition the elements into batches of {@code windowSize} elements (by using a
     * step width equal to the specified window size) or to sample every n-th element (by using a window size
     * of 1 and a step width of n).
     *
     * <p>This is an intermediate operation.
     *
     * <p>Examples: <pre>
     * elements: [1, 1, 1, 2, 2, 2, 3, 3, 3]    windowSize: 3   stepWidth: 3
     *
     * =&gt; [1, 1, 1], [2, 2, 2] [3, 3, 3]
     *
     *
     * elements: [1, 2, 3, 1, 2, 3, 1, 2, 3]    windowSize: 2   stepWidth: 3
     *
     * =&gt; [1, 2], [1, 2], [1, 2]
     *
     *
     * elements: [1, 2, 3, 4, 5, 6]             windowSize: 3   stepWidth: 1
     *
     * =&gt; [1, 2, 3], [2, 3, 4], [3, 4, 5], [4, 5, 6]
     * </pre>
     *
     * @param windowSize  number of elements that will be emitted together in a list
     * @param stepWidth  step width
     * @return the new stream
     * @throws IllegalArgumentException if {@code windowSize} is zero or negative
     * @throws IllegalArgumentException if {@code stepWidth} is zero or negative
     */
    public Stream<List<T>> slidingWindow(final int windowSize, final int stepWidth) {
        if (windowSize <= 0) throw new IllegalArgumentException("windowSize cannot be zero or negative");
        if (stepWidth <= 0) throw new IllegalArgumentException("stepWidth cannot be zero or negative");
        return new Stream<List<T>>(new LsaIterator<List<T>>() {
            private final Queue<T> queue = Compat.queue();

            @Override
            public boolean hasNext() {
                return iterator.hasNext();
            }

            @Override
            public List<T> nextIteration() {
                int i = queue.size();
                while (i < windowSize && iterator.hasNext()) {
                    queue.offer(iterator.next());
                    i++;
                }

                // the elements that are currently in the queue are the elements of our current window
                List<T> list = new ArrayList<T>(queue);

                // remove stepWidth elements from the queue
                final int pollCount = Math.min(queue.size(), stepWidth);
                for (int j = 0; j < pollCount; j++) {
                    queue.poll();
                }

                // if the stepWidth is greater than the windowSize, skip (stepWidth - windowSize) elements
                for (int j = windowSize; j < stepWidth && iterator.hasNext(); j++) {
                    iterator.next();
                }

                return list;
            }
        });
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
            public T nextIteration() {
                final T value = iterator.next();
                action.accept(value);
                return value;
            }
        });
    }

    /**
     * Takes elements while the predicate is true.
     *
     * <p>This is an intermediate operation.
     *
     * @param predicate  the predicate used to take elements
     * @return the new stream
     */
    public Stream<T> takeWhile(final Predicate<? super T> predicate) {
        return new Stream<T>(new LsaExtIterator<T>() {

            @Override
            protected void nextIteration() {
                hasNext = iterator.hasNext() && predicate.test(next = iterator.next());
            }
        });
    }

    /**
     * Drops elements while the predicate is true and returns the rest.
     *
     * <p>This is an intermediate operation.
     *
     * @param predicate  the predicate used to drop elements
     * @return the new stream
     */
    public Stream<T> dropWhile(final Predicate<? super T> predicate) {
        return new Stream<T>(new LsaExtIterator<T>() {

            @Override
            protected void nextIteration() {
                if (!isInit) {
                    // Skip first time
                    while (hasNext = iterator.hasNext()) {
                        next = iterator.next();
                        if (!predicate.test(next)) {
                            return;
                        }
                    }
                }

                hasNext = hasNext && iterator.hasNext();
                if (!hasNext) return;

                next = iterator.next();
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
     * @throws IllegalArgumentException if {@code maxSize} is negative
     */
    public Stream<T> limit(final long maxSize) {
        if (maxSize < 0) {
            throw new IllegalArgumentException("maxSize cannot be negative");
        }
        if (maxSize == 0) {
            return Stream.empty();
        }
        return new Stream<T>(new LsaIterator<T>() {

            private long index = 0;

            @Override
            public boolean hasNext() {
                return (index < maxSize) && iterator.hasNext();
            }

            @Override
            public T nextIteration() {
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
     * @throws IllegalArgumentException if {@code n} is negative
     */
    public Stream<T> skip(final long n) {
        if (n < 0) {
            throw new IllegalArgumentException("n cannot be negative");
        }
        if (n == 0) {
            return this;
        }
        return new Stream<T>(new LsaIterator<T>() {

            private long skippedCount;

            @Override
            public boolean hasNext() {
                while (skippedCount < n) {
                    if (!iterator.hasNext()) return false;
                    iterator.next();
                    skippedCount++;
                }
                return iterator.hasNext();
            }

            @Override
            public T nextIteration() {
                return iterator.next();
            }
        });
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
        return foundAny ? Optional.of(result) : Optional.<T>empty();
    }

    /**
     * Collects elements to an array.
     *
     * <p>This is a terminal operation.
     *
     * @return the result of collect elements
     * @see #toArray(com.annimon.stream.function.IntFunction)
     */
    public Object[] toArray() {
        return toArray(new IntFunction<Object[]>() {

            @Override
            public Object[] apply(int value) {
                return new Object[value];
            }
        });
    }

    /**
     * Collects elements to an array, the {@code generator} constructor of provided.
     *
     * <p>This is a terminal operation.
     *
     * @param <R> the type of the result
     * @param generator  the array constructor reference that accommodates future array of assigned size
     * @return the result of collect elements
     */
    @SuppressWarnings("unchecked")
    public <R> R[] toArray(IntFunction<R[]> generator) {
        final List<T> container = collectToList();
        final int size = container.size();

        if (size >= MAX_ARRAY_SIZE) throw new IllegalArgumentException(BAD_SIZE);

        //noinspection unchecked
        T[] source = container.toArray(Compat.<T>newArray(size));
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
        return Collectors.<A, R>castIdentity().apply(container);
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
        return reduce(BinaryOperator.Util.<T>minBy(comparator));
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
        return reduce(BinaryOperator.Util.<T>maxBy(comparator));
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
     * If stream is empty, returns {@code Optional.empty()}.
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

    /**
     * Returns the single element of stream.
     * If stream is empty, throws {@code NoSuchElementException}.
     * If stream contains more than one element, throws {@code IllegalStateException}.
     *
     * <p>This is a short-circuiting terminal operation.
     *
     * @return single element of stream
     * @throws NoSuchElementException if stream is empty
     * @throws IllegalStateException if stream contains more than one element
     * @since 1.1.2
     */
    public T single() {
        if (iterator.hasNext()) {
            T singleCandidate = iterator.next();
            if (iterator.hasNext()) {
                throw new IllegalStateException("Stream contains more than one element");
            } else {
                return singleCandidate;
            }
        } else {
            throw new NoSuchElementException("Stream contains no element");
        }
    }

    /**
     * Returns the single element wrapped by {@code Optional} class.
     * If stream is empty, returns {@code Optional.empty()}.
     * If stream contains more than one element, throws {@code IllegalStateException}.
     *
     * <p>This is a short-circuiting terminal operation.
     *
     * @return an {@code Optional} with single element or {@code Optional.empty()} if stream is empty
     * @throws IllegalStateException if stream contains more than one element
     * @since 1.1.2
     */
    public Optional<T> findSingle() {
        if (iterator.hasNext()) {
            T singleCandidate = iterator.next();
            if (iterator.hasNext()) {
                throw new IllegalStateException("Stream contains more than one element");
            } else {
                return Optional.of(singleCandidate);
            }
        } else {
            return Optional.empty();
        }
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

    private List<T> collectToList() {
        final List<T> container = new ArrayList<T>();
        while (iterator.hasNext()) {
            container.add(iterator.next());
        }
        return container;
    }

//</editor-fold>
}
