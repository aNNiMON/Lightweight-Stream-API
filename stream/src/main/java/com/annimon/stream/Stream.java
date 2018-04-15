package com.annimon.stream;

import com.annimon.stream.function.*;
import com.annimon.stream.internal.Compose;
import com.annimon.stream.internal.Operators;
import com.annimon.stream.internal.Params;
import com.annimon.stream.iterator.IndexedIterator;
import com.annimon.stream.iterator.LazyIterator;
import com.annimon.stream.operator.*;
import java.io.Closeable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

/**
 * A sequence of elements supporting aggregate operations.
 *
 * @param <T> the type of the stream elements
 */
public class Stream<T> implements Closeable {

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
        if (elements.length == 0) {
            return Stream.<T>empty();
        }
        return new Stream<T>(new ObjArray<T>(elements));
    }

    /**
     * If specified element is null, returns an empty {@code Stream},
     * otherwise returns a {@code Stream} containing a single element.
     *
     * @param <T> the type of the stream element
     * @param element  the element to be passed to stream if it is non-null
     * @return the new stream
     * @since 1.1.5
     */
    @SuppressWarnings("unchecked")
    public static <T> Stream<T> ofNullable(T element) {
        return (element == null) ? Stream.<T>empty() : Stream.of(element);
    }

    /**
     * If specified array is null, returns an empty {@code Stream},
     * otherwise returns a {@code Stream} containing elements of this array.
     *
     * @param <T> the type of the stream elements
     * @param array  the array whose elements to be passed to stream
     * @return the new stream
     * @since 1.1.9
     */
    public static <T> Stream<T> ofNullable(final T[] array) {
        return (array == null) ? Stream.<T>empty() : Stream.of(array);
    }

    /**
     * If specified map is null, returns an empty {@code Stream},
     * otherwise returns a {@code Stream} containing entries of this map.
     *
     * @param <K> the type of map keys
     * @param <V> the type of map values
     * @param map  the map with elements to be passed to stream
     * @return the new stream
     * @since 1.1.9
     */
    public static <K, V> Stream<Map.Entry<K, V>> ofNullable(Map<K, V> map) {
        return (map == null) ? Stream.<Map.Entry<K, V>>empty() : Stream.of(map);
    }

    /**
     * If specified iterator is null, returns an empty {@code Stream},
     * otherwise returns a {@code Stream} containing entries of this iterator.
     *
     * @param <T> the type of the stream elements
     * @param iterator  the iterator with elements to be passed to stream
     * @return the new stream
     * @since 1.1.9
     */
    public static <T> Stream<T> ofNullable(Iterator<? extends T> iterator) {
        return (iterator == null) ? Stream.<T>empty() : Stream.of(iterator);
    }

    /**
     * If specified iterable is null, returns an empty {@code Stream},
     * otherwise returns a {@code Stream} containing elements of this iterable.
     *
     * @param <T> the type of the stream elements
     * @param iterable  the {@code Iterable} with elements to be passed to stream
     * @return the new stream
     * @since 1.1.5
     */
    public static <T> Stream<T> ofNullable(Iterable<? extends T> iterable) {
        return (iterable == null) ? Stream.<T>empty() : Stream.of(iterable);
    }

    /**
     * Creates a {@code Stream<Integer>} from not closed range
     * (from {@code from} inclusive to {@code to} exclusive and incremental step {@code 1}).
     *
     * @param from  the initial value (inclusive)
     * @param to  the upper bound (exclusive)
     * @return the new stream
     * @see IntStream#range(int, int)
     */
    public static Stream<Integer> range(final int from, final int to) {
        return IntStream.range(from, to).boxed();
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
        return LongStream.range(from, to).boxed();
    }

    /**
     * Creates a {@code Stream<Integer>} from closed range
     * (from {@code from} inclusive to {@code to} inclusive and incremental step {@code 1}).
     *
     * @param from  the initial value (inclusive)
     * @param to  the upper bound (inclusive)
     * @return the new stream
     * @see IntStream#rangeClosed(int, int)
     */
    public static Stream<Integer> rangeClosed(final int from, final int to) {
        return IntStream.rangeClosed(from, to).boxed();
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
        return LongStream.rangeClosed(from, to).boxed();
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
        return new Stream<T>(new ObjGenerate<T>(supplier));
    }

    /**
     * Creates a {@code Stream} by iterative application {@code UnaryOperator} function
     * to an initial element {@code seed}. Produces {@code Stream} consisting of
     * {@code seed}, {@code op(seed)}, {@code op(op(seed))}, etc.
     *
     * <p>Example:
     * <pre>
     * seed: 1
     * op: (a) -&gt; a + 5
     * result: [1, 6, 11, 16, ...]
     * </pre>
     *
     * @param <T> the type of the stream elements
     * @param seed  the initial value
     * @param op  operator to produce new element by previous one
     * @return the new stream
     * @throws NullPointerException if {@code op} is null
     */
    public static <T> Stream<T> iterate(final T seed, final UnaryOperator<T> op) {
        Objects.requireNonNull(op);
        return new Stream<T>(new ObjIterate<T>(seed, op));
    }

    /**
     * Creates a {@code Stream} by iterative application {@code UnaryOperator} function
     * to an initial element {@code seed}, conditioned on satisfying the supplied predicate.
     *
     * <p>Example:
     * <pre>
     * seed: 0
     * predicate: (a) -&gt; a &lt; 20
     * op: (a) -&gt; a + 5
     * result: [0, 5, 10, 15]
     * </pre>
     *
     * @param <T> the type of the stream elements
     * @param seed  the initial value
     * @param predicate  a predicate to determine when the stream must terminate
     * @param op  operator to produce new element by previous one
     * @return the new stream
     * @throws NullPointerException if {@code op} is null
     * @since 1.1.5
     */
    public static <T> Stream<T> iterate(final T seed,
            final Predicate<? super T> predicate, final UnaryOperator<T> op) {
        Objects.requireNonNull(predicate);
        return iterate(seed, op).takeWhile(predicate);
    }

    /**
     * Concatenates two streams.
     *
     * <p>Example:
     * <pre>
     * stream 1: [1, 2, 3, 4]
     * stream 2: [5, 6]
     * result:   [1, 2, 3, 4, 5, 6]
     * </pre>
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
        Stream<T> result = new Stream<T>(new ObjConcat<T>(stream1.iterator, stream2.iterator));
        return result.onClose(Compose.closeables(stream1, stream2));
    }

    /**
     * Concatenates two iterators to a stream.
     *
     * <p>Example:
     * <pre>
     * iterator 1: [1, 2, 3, 4]
     * iterator 2: [5, 6]
     * result:     [1, 2, 3, 4, 5, 6]
     * </pre>
     *
     * @param <T> The type of iterator elements
     * @param iterator1  the first iterator
     * @param iterator2  the second iterator
     * @return the new stream
     * @throws NullPointerException if {@code iterator1} or {@code iterator2} is null
     * @since 1.1.9
     */
    public static <T> Stream<T> concat(Iterator<? extends T> iterator1, Iterator<? extends T> iterator2) {
        Objects.requireNonNull(iterator1);
        Objects.requireNonNull(iterator2);
        return new Stream<T>(new ObjConcat<T>(iterator1, iterator2));
    }

    /**
     * Combines two streams by applying specified combiner function to each element at same position.
     *
     * <p>Example:
     * <pre>
     * combiner: (a, b) -&gt; a + b
     * stream 1: [1, 2, 3, 4]
     * stream 2: [5, 6, 7, 8]
     * result:   [6, 8, 10, 12]
     * </pre>
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
        return Stream.<F, S, R>zip(stream1.iterator, stream2.iterator, combiner);
    }

    /**
     * Combines two iterators to a stream by applying specified combiner function to each element at same position.
     *
     * <p>Example:
     * <pre>
     * combiner: (a, b) -&gt; a + b
     * stream 1: [1, 2, 3, 4]
     * stream 2: [5, 6, 7, 8]
     * result:   [6, 8, 10, 12]
     * </pre>
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
        return new Stream<R>(new ObjZip<F, S, R>(iterator1, iterator2, combiner));
    }

    /**
     * Merges elements of two streams according to the supplied selector function.
     *
     * <p>Example 1 — Merge two sorted streams:
     * <pre>
     * stream1: [1, 3, 8, 10]
     * stream2: [2, 5, 6, 12]
     * selector: (a, b) -&gt; a &lt; b ? TAKE_FIRST : TAKE_SECOND
     * result: [1, 2, 3, 5, 6, 8, 10, 12]
     * </pre>
     *
     * <p>Example 2 — Concat two streams:
     * <pre>
     * stream1: [0, 3, 1]
     * stream2: [2, 5, 6, 1]
     * selector: (a, b) -&gt; TAKE_SECOND
     * result: [2, 5, 6, 1, 0, 3, 1]
     * </pre>
     *
     * @param <T> the type of the elements
     * @param stream1  the first stream
     * @param stream2  the second stream
     * @param selector the selector function used to choose elements
     * @return the new stream
     * @throws NullPointerException if {@code stream1} or {@code stream2} is null
     * @since 1.1.9
     */
    public static <T> Stream<T> merge(
            Stream<? extends T> stream1, Stream<? extends T> stream2,
            BiFunction<? super T, ? super T, ObjMerge.MergeResult> selector) {
        Objects.requireNonNull(stream1);
        Objects.requireNonNull(stream2);
        return Stream.<T>merge(stream1.iterator, stream2.iterator, selector);
    }

    /**
     * Merges elements of two iterators according to the supplied selector function.
     *
     * <p>Example 1 — Merge two sorted iterators:
     * <pre>
     * iterator1: [1, 3, 8, 10]
     * iterator2: [2, 5, 6, 12]
     * selector: (a, b) -&gt; a &lt; b ? TAKE_FIRST : TAKE_SECOND
     * result: [1, 2, 3, 5, 6, 8, 10, 12]
     * </pre>
     *
     * <p>Example 2 — Concat two iterators:
     * <pre>
     * iterator1: [0, 3, 1]
     * iterator2: [2, 5, 6, 1]
     * selector: (a, b) -&gt; TAKE_SECOND
     * result: [2, 5, 6, 1, 0, 3, 1]
     * </pre>
     *
     * @param <T> the type of the elements
     * @param iterator1  the first iterator
     * @param iterator2  the second iterator
     * @param selector  the selector function used to choose elements
     * @return the new stream
     * @throws NullPointerException if {@code iterator1} or {@code iterator2} is null
     * @since 1.1.9
     */
    public static <T> Stream<T> merge(
            Iterator<? extends T> iterator1, Iterator<? extends T> iterator2,
            BiFunction<? super T, ? super T, ObjMerge.MergeResult> selector) {
        Objects.requireNonNull(iterator1);
        Objects.requireNonNull(iterator2);
        return new Stream<T>(new ObjMerge<T>(iterator1, iterator2, selector));
    }


//<editor-fold defaultstate="collapsed" desc="Implementation">
    private final Iterator<? extends T> iterator;
    private final Params params;

    private Stream(Iterator<? extends T> iterator) {
        this(null, iterator);
    }

    private Stream(Iterable<? extends T> iterable) {
        this(null, new LazyIterator<T>(iterable));
    }

    private Stream(Params params, Iterable<? extends T> iterable) {
        this(params, new LazyIterator<T>(iterable));
    }

    Stream(Params params, Iterator<? extends T> iterator) {
        this.params = params;
        this.iterator = iterator;
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
     * <p>Example:
     * <pre>
     * predicate: (a) -&gt; a &gt; 2
     * stream: [1, 2, 3, 4, -8, 0, 11]
     * result: [3, 4, 11]
     * </pre>
     *
     * @param predicate  the predicate used to filter elements
     * @return the new stream
     */
    public Stream<T> filter(final Predicate<? super T> predicate) {
        return new Stream<T>(params, new ObjFilter<T>(iterator, predicate));
    }

    /**
     * Returns a {@code Stream} with elements that satisfy the given {@code IndexedPredicate}.
     *
     * <p>This is an intermediate operation.
     *
     * <p>Example:
     * <pre>
     * predicate: (index, value) -&gt; (index + value) &gt; 6
     * stream: [1, 2, 3, 4, 0, 11]
     * index:  [0, 1, 2, 3, 4,  5]
     * sum:    [1, 3, 5, 7, 4, 16]
     * filter: [         7,    16]
     * result: [4, 11]
     * </pre>
     *
     * @param predicate  the {@code IndexedPredicate} used to filter elements
     * @return the new stream
     * @since 1.1.6
     */
    public Stream<T> filterIndexed(IndexedPredicate<? super T> predicate) {
        return filterIndexed(0, 1, predicate);
    }

    /**
     * Returns a {@code Stream} with elements that satisfy the given {@code IndexedPredicate}.
     *
     * <p>This is an intermediate operation.
     *
     * <p>Example:
     * <pre>
     * from: 4
     * step: 3
     * predicate: (index, value) -&gt; (index + value) &gt; 15
     * stream: [1, 2,  3,  4,  0, 11]
     * index:  [4, 7, 10, 13, 16, 19]
     * sum:    [5, 9, 13, 17, 16, 30]
     * filter: [          17, 16, 30]
     * result: [4, 0, 11]
     * </pre>
     *
     * @param from  the initial value of the index (inclusive)
     * @param step  the step of the index
     * @param predicate  the {@code IndexedPredicate} used to filter elements
     * @return the new stream
     * @since 1.1.6
     */
    public Stream<T> filterIndexed(int from, int step, IndexedPredicate<? super T> predicate) {
        return new Stream<T>(params, new ObjFilterIndexed<T>(
                new IndexedIterator<T>(from, step, iterator),
                predicate));
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
     * @return the new stream of type passed as parameter
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
     * Returns {@code Stream} without null elements.
     *
     * <p>This is an intermediate operation.
     *
     * @return the new stream
     * @since 1.1.6
     */
    public Stream<T> withoutNulls() {
        return filter(Predicate.Util.<T>notNull());
    }

    /**
     * Returns {@code Stream} with elements that is null only.
     *
     * <p>This is an intermediate operation.
     *
     * @return the new stream
     * @since 1.1.6
     */
    public Stream<T> nullsOnly() {
        return filterNot(Predicate.Util.<T>notNull());
    }

    /**
     * Returns {@code Stream} with elements that is equality of {@code object} only.
     *
     * <p>This is an intermediate operation.
     *
     * @param object object
     * @return the new stream
     * @since 1.2.0
     */
    public Stream<T> equalsOnly(final T object) {
        return filter(new Predicate<T>() {
            @Override
            public boolean test(T value) {
                return Objects.equals(value, object);
            }
        });
    }

    /**
     * Returns {@code Stream} with elements that obtained by applying the given function.
     *
     * <p>This is an intermediate operation.
     *
     * <p>Example:
     * <pre>
     * mapper: (a) -&gt; a + 5
     * stream: [1, 2, 3, 4]
     * result: [6, 7, 8, 9]
     * </pre>
     *
     * @param <R> the type of elements in resulting stream
     * @param mapper  the mapper function used to apply to each element
     * @return the new stream
     */
    public <R> Stream<R> map(final Function<? super T, ? extends R> mapper) {
        return new Stream<R>(params, new ObjMap<T, R>(iterator, mapper));
    }

    /**
     * Returns a {@code Stream} with elements that obtained by applying the given {@code IndexedFunction}.
     *
     * <p>This is an intermediate operation.
     *
     * <p>Example:
     * <pre>
     * predicate: (index, value) -&gt; (index * value)
     * stream: [1, 2, 3,  4]
     * index:  [0, 1, 2,  3]
     * result: [0, 2, 6, 12]
     * </pre>
     *
     * @param <R> the type of elements in resulting stream
     * @param mapper  the mapper function used to apply to each element
     * @return the new stream
     * @since 1.1.6
     */
    public <R> Stream<R> mapIndexed(IndexedFunction<? super T, ? extends R> mapper) {
        return this.<R>mapIndexed(0, 1, mapper);
    }

    /**
     * Returns a {@code Stream} with elements that obtained by applying the given {@code IndexedFunction}.
     *
     * <p>This is an intermediate operation.
     *
     * <p>Example:
     * <pre>
     * from: -2
     * step: 2
     * predicate: (index, value) -&gt; (index * value)
     * stream: [ 1, 2, 3,  4]
     * index:  [-2, 0, 2,  4]
     * result: [-2, 0, 6, 16]
     * </pre>
     *
     * @param <R> the type of elements in resulting stream
     * @param from  the initial value of the index (inclusive)
     * @param step  the step of the index
     * @param mapper  the mapper function used to apply to each element
     * @return the new stream
     * @since 1.1.6
     */
    public <R> Stream<R> mapIndexed(int from, int step, IndexedFunction<? super T, ? extends R> mapper) {
        return new Stream<R>(params, new ObjMapIndexed<T, R>(
                new IndexedIterator<T>(from, step, iterator),
                mapper));
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
        return new IntStream(params, new ObjMapToInt<T>(iterator, mapper));
    }

    /**
     * Returns {@code LongStream} with elements that obtained by applying the given function.
     *
     * <p>This is an intermediate operation.
     *
     * @param mapper  the mapper function used to apply to each element
     * @return the new {@code LongStream}
     * @since 1.1.4
     * @see #map(com.annimon.stream.function.Function)
     */
    public LongStream mapToLong(final ToLongFunction<? super T> mapper) {
        return new LongStream(params, new ObjMapToLong<T>(iterator, mapper));
    }

    /**
     * Returns {@code DoubleStream} with elements that obtained by applying the given function.
     *
     * <p>This is an intermediate operation.
     *
     * @param mapper  the mapper function used to apply to each element
     * @return the new {@code DoubleStream}
     * @since 1.1.4
     * @see #map(com.annimon.stream.function.Function)
     */
    public DoubleStream mapToDouble(final ToDoubleFunction<? super T> mapper) {
        return new DoubleStream(params, new ObjMapToDouble<T>(iterator, mapper));
    }

    /**
     * Returns a stream consisting of the results of replacing each element of
     * this stream with the contents of a mapped stream produced by applying
     * the provided mapping function to each element.
     *
     * <p>This is an intermediate operation.
     *
     * <p>Example:
     * <pre>
     * mapper: (a) -&gt; [a, a + 5]
     * stream: [1, 2, 3, 4]
     * result: [1, 6, 2, 7, 3, 8, 4, 9]
     * </pre>
     *
     * @param <R> the type of elements in resulting stream
     * @param mapper  the mapper function used to apply to each element
     * @return the new stream
     */
    public <R> Stream<R> flatMap(final Function<? super T, ? extends Stream<? extends R>> mapper) {
        return new Stream<R>(params, new ObjFlatMap<T, R>(iterator, mapper));
    }

    /**
     * Returns a stream consisting of the results of replacing each element of
     * this stream with the contents of a mapped stream produced by applying
     * the provided mapping function to each element.
     *
     * <p>This is an intermediate operation.
     *
     * @param mapper  the mapper function used to apply to each element
     * @return the new {@code IntStream}
     * @see #flatMap(com.annimon.stream.function.Function)
     */
    public IntStream flatMapToInt(final Function<? super T, ? extends IntStream> mapper) {
        return new IntStream(params, new ObjFlatMapToInt<T>(iterator, mapper));
    }

    /**
     * Returns a stream consisting of the results of replacing each element of
     * this stream with the contents of a mapped stream produced by applying
     * the provided mapping function to each element.
     *
     * <p>This is an intermediate operation.
     *
     * @param mapper  the mapper function used to apply to each element
     * @return the new {@code LongStream}
     * @see #flatMap(com.annimon.stream.function.Function)
     */
    public LongStream flatMapToLong(final Function<? super T, ? extends LongStream> mapper) {
        return new LongStream(params, new ObjFlatMapToLong<T>(iterator, mapper));
    }

    /**
     * Returns a stream consisting of the results of replacing each element of
     * this stream with the contents of a mapped stream produced by applying
     * the provided mapping function to each element.
     *
     * <p>This is an intermediate operation.
     *
     * @param mapper  the mapper function used to apply to each element
     * @return the new {@code DoubleStream}
     * @see #flatMap(com.annimon.stream.function.Function)
     */
    public DoubleStream flatMapToDouble(final Function<? super T, ? extends DoubleStream> mapper) {
        return new DoubleStream(params, new ObjFlatMapToDouble<T>(iterator, mapper));
    }

    /**
     * Returns {@code Stream} with indexed elements.
     * Indexing starts from 0 with step 1.
     *
     * <p>This is an intermediate operation.
     *
     * <p>Example:
     * <pre>
     * stream: ["a", "b", "c"]
     * result: [(0, "a"), (1, "b"), (2, "c")]
     * </pre>
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
     * <p>Example:
     * <pre>
     * from: 5, step: 10
     * stream: ["a", "b", "c"]
     * result: [(5, "a"), (15, "b"), (25, "c")]
     * </pre>
     *
     * @param from  the initial value (inclusive)
     * @param step  the step
     * @return the new {@code IntPair} stream
     * @since 1.1.2
     */
    public Stream<IntPair<T>> indexed(final int from, final int step) {
        return mapIndexed(from, step, new IndexedFunction<T, IntPair<T>>() {

            @Override
            public IntPair<T> apply(int index, T t) {
                return new IntPair<T>(index, t);
            }
        });
    }

    /**
     * Returns {@code Stream} with distinct elements (as determined by {@code hashCode} and {@code equals} methods).
     *
     * <p>This is a stateful intermediate operation.
     *
     * <p>Example:
     * <pre>
     * stream: [1, 4, 2, 3, 3, 4, 1]
     * result: [1, 4, 2, 3]
     * </pre>
     *
     * @return the new stream
     */
    public Stream<T> distinct() {
        return new Stream<T>(params, new ObjDistinct<T>(iterator));
    }

    /**
     * Returns {@code Stream} with distinct elements (as determined by {@code hashCode}
     * and {@code equals} methods) according to the given classifier function.
     *
     * <p>This is a stateful intermediate operation.
     *
     * <p>Example:
     * <pre>
     * classifier: (str) -&gt; str.length()
     * stream: ["a", "bc", "d", "ef", "ghij"]
     * result: ["a", "bc", "ghij"]
     * </pre>
     *
     * @param <K> the type of the result of classifier function
     * @param classifier  the classifier function
     * @return the new stream
     * @since 1.1.8
     */
    public <K> Stream<T> distinctBy(Function<? super T, ? extends K> classifier) {
        return new Stream<T>(params, new ObjDistinctBy<T, K>(iterator, classifier));
    }

    /**
     * Returns {@code Stream} with sorted elements (as determinated by {@link Comparable} interface).
     *
     * <p>This is a stateful intermediate operation.
     * <p>If the elements of this stream are not {@link Comparable},
     * a {@code java.lang.ClassCastException} may be thrown when the terminal operation is executed.
     *
     * <p>Example:
     * <pre>
     * stream: [3, 4, 1, 2]
     * result: [1, 2, 3, 4]
     * </pre>
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
     * <p>Example:
     * <pre>
     * comparator: (a, b) -&gt; -a.compareTo(b)
     * stream: [1, 2, 3, 4]
     * result: [4, 3, 2, 1]
     * </pre>
     *
     * @param comparator  the {@code Comparator} to compare elements
     * @return the new stream
     */
    public Stream<T> sorted(final Comparator<? super T> comparator) {
        return new Stream<T>(params, new ObjSorted<T>(iterator, comparator));
    }

    /**
     * Returns {@code Stream} with sorted elements (as determinated by {@code Comparable} interface).
     * Each element transformed by given function {@code f} before comparing.
     *
     * <p>This is a stateful intermediate operation.
     *
     * <p>Example:
     * <pre>
     * f: (a) -&gt; -a
     * stream: [1, 2, 3, 4]
     * result: [4, 3, 2, 1]
     * </pre>
     *
     * @param <R> the type of the result of transforming function
     * @param f  the transformation function
     * @return the new stream
     */
    public <R extends Comparable<? super R>> Stream<T> sortBy(final Function<? super T, ? extends R> f) {
        return sorted(ComparatorCompat.comparing(f));
    }

    /**
     * Partitions {@code Stream} into {@code Map} entries according to the given classifier function.
     *
     * <p>This is a stateful intermediate operation.
     *
     * <p>Example:
     * <pre>
     * classifier: (str) -&gt; str.length()
     * stream: ["a", "bc", "d", "ef", "ghij"]
     * result: [{1: ["a", "d"]}, {2: ["bc", "ef"]}, {4: ["ghij"]}]
     * </pre>
     *
     * @param <K> the type of the keys, which are result of the classifier function
     * @param classifier  the classifier function
     * @return the new stream
     */
    public <K> Stream<Map.Entry<K, List<T>>> groupBy(final Function<? super T, ? extends K> classifier) {
        Map<K, List<T>> map = collect(Collectors.<T, K>groupingBy(classifier));
        return new Stream<Map.Entry<K, List<T>>>(params, map.entrySet());
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
     * <p>Example:
     * <pre>
     * classifier: (a) -&gt; a % 5 == 0
     * stream: [1, 2, 5, 6, 7, 9, 10, 12, 14]
     * result: [[1, 2], [5], [6, 7, 9], [10], [12, 14]]
     * </pre>
     *
     * @param <K> the type of the keys, which are the result of the classifier function
     * @param classifier  the classifier function
     * @return the new stream
     */
    public <K> Stream<List<T>> chunkBy(final Function<? super T, ? extends K> classifier) {
        return new Stream<List<T>>(params, new ObjChunkBy<T, K>(iterator, classifier));
    }

    /**
     * Samples the {@code Stream} by emitting every n-th element.
     *
     * <p>This is an intermediate operation.
     *
     * <p>Example:
     * <pre>
     * stepWidth: 3
     * stream: [1, 2, 3, 4, 5, 6, 7, 8]
     * result: [1, 4, 7]
     * </pre>
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
     * <p>Example:
     * <pre>
     * windowSize: 3
     * stream: [1, 2, 3, 4, 5]
     * result: [[1, 2, 3], [2, 3, 4], [3, 4, 5]]
     * </pre>
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
     * <p>Example:
     * <pre>
     * windowSize: 3, stepWidth: 3
     * stream: [1, 1, 1, 2, 2, 2, 3, 3, 3]
     * result: [[1, 1, 1], [2, 2, 2] [3, 3, 3]]
     *
     * windowSize: 2, stepWidth: 3
     * stream: [1, 2, 3, 1, 2, 3, 1, 2, 3]
     * result: [[1, 2], [1, 2], [1, 2]]
     *
     * windowSize: 3, stepWidth: 1
     * stream: [1, 2, 3, 4, 5, 6]
     * result: [[1, 2, 3], [2, 3, 4], [3, 4, 5], [4, 5, 6]]
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
        return new Stream<List<T>>(params, new ObjSlidingWindow<T>(iterator, windowSize, stepWidth));
    }

    /**
     * Performs provided action on each element.
     *
     * <p>This is an intermediate operation.
     *
     * @param action  the action to be performed on each element
     * @return the new stream
     */
    public Stream<T> peek(final Consumer<? super T> action) {
        return new Stream<T>(params, new ObjPeek<T>(iterator, action));
    }

    /**
     * Returns a {@code Stream} produced by iterative application of a accumulation function
     * to reduction value and next element of the current stream.
     * Produces a {@code Stream} consisting of {@code value1}, {@code acc(value1, value2)},
     * {@code acc(acc(value1, value2), value3)}, etc.
     *
     * <p>This is an intermediate operation.
     *
     * <p>Example:
     * <pre>
     * accumulator: (a, b) -&gt; a + b
     * stream: [1, 2, 3, 4, 5]
     * result: [1, 3, 6, 10, 15]
     * </pre>
     *
     * @param accumulator  the accumulation function
     * @return the new stream
     * @throws NullPointerException if {@code accumulator} is null
     * @since 1.1.6
     */
    public Stream<T> scan(final BiFunction<T, T, T> accumulator) {
        Objects.requireNonNull(accumulator);
        return new Stream<T>(params, new ObjScan<T>(iterator, accumulator));
    }

    /**
     * Returns a {@code Stream} produced by iterative application of a accumulation function
     * to an initial element {@code identity} and next element of the current stream.
     * Produces a {@code Stream} consisting of {@code identity}, {@code acc(identity, value1)},
     * {@code acc(acc(identity, value1), value2)}, etc.
     *
     * <p>This is an intermediate operation.
     *
     * <p>Example:
     * <pre>
     * identity: 0
     * accumulator: (a, b) -&gt; a + b
     * stream: [1, 2, 3, 4, 5]
     * result: [0, 1, 3, 6, 10, 15]
     * </pre>
     *
     * @param <R> the type of the result
     * @param identity  the initial value
     * @param accumulator  the accumulation function
     * @return the new stream
     * @throws NullPointerException if {@code accumulator} is null
     * @since 1.1.6
     */
    public <R> Stream<R> scan(final R identity, final BiFunction<? super R, ? super T, ? extends R> accumulator) {
        Objects.requireNonNull(accumulator);
        return new Stream<R>(params, new ObjScanIdentity<T, R>(iterator, identity, accumulator));
    }

    /**
     * Takes elements while the predicate returns {@code true}.
     *
     * <p>This is an intermediate operation.
     *
     * <p>Example:
     * <pre>
     * predicate: (a) -&gt; a &lt; 3
     * stream: [1, 2, 3, 4, 1, 2, 3, 4]
     * result: [1, 2]
     * </pre>
     *
     * @param predicate  the predicate used to take elements
     * @return the new stream
     */
    public Stream<T> takeWhile(final Predicate<? super T> predicate) {
        return new Stream<T>(params, new ObjTakeWhile<T>(iterator, predicate));
    }

    /**
     * Takes elements while the {@code IndexedPredicate} returns {@code true}.
     *
     * <p>This is an intermediate operation.
     *
     * <p>Example:
     * <pre>
     * predicate: (index, value) -&gt; (index + value) &lt; 5
     * stream: [1, 2, 3,  4, -5, -6, -7]
     * index:  [0, 1, 2,  3,  4,  5,  6]
     * sum:    [1, 3, 5,  7, -1, -1, -1]
     * result: [1, 2]
     * </pre>
     *
     * @param predicate  the {@code IndexedPredicate} used to take elements
     * @return the new stream
     * @since 1.1.6
     */
    public Stream<T> takeWhileIndexed(IndexedPredicate<? super T> predicate) {
        return takeWhileIndexed(0, 1, predicate);
    }

    /**
     * Takes elements while the {@code IndexedPredicate} returns {@code true}.
     *
     * <p>This is an intermediate operation.
     *
     * <p>Example:
     * <pre>
     * from: 2
     * step: 2
     * predicate: (index, value) -&gt; (index + value) &lt; 8
     * stream: [1, 2, 3,  4, -5, -6, -7]
     * index:  [2, 4, 6,  8, 10, 12, 14]
     * sum:    [3, 6, 9, 12,  5,  6,  7]
     * result: [1, 2]
     * </pre>
     *
     * @param from  the initial value of the index (inclusive)
     * @param step  the step of the index
     * @param predicate  the {@code IndexedPredicate} used to take elements
     * @return the new stream
     * @since 1.1.6
     */
    public Stream<T> takeWhileIndexed(int from, int step, IndexedPredicate<? super T> predicate) {
        return new Stream<T>(params, new ObjTakeWhileIndexed<T>(
                new IndexedIterator<T>(from, step, iterator),
                predicate));
    }

    /**
     * Takes elements while the predicate returns {@code false}.
     * Once predicate condition is satisfied by an element, the stream
     * finishes with this element.
     *
     * <p>This is an intermediate operation.
     *
     * <p>Example:
     * <pre>
     * stopPredicate: (a) -&gt; a &gt; 2
     * stream: [1, 2, 3, 4, 1, 2, 3, 4]
     * result: [1, 2, 3]
     * </pre>
     *
     * @param stopPredicate  the predicate used to take elements
     * @return the new stream
     * @since 1.1.6
     */
    public Stream<T> takeUntil(final Predicate<? super T> stopPredicate) {
        return new Stream<T>(params, new ObjTakeUntil<T>(iterator, stopPredicate));
    }

    /**
     * Takes elements while the {@code IndexedPredicate} returns {@code false}.
     * Once predicate condition is satisfied by an element, the stream
     * finishes with this element.
     *
     * <p>This is an intermediate operation.
     *
     * <p>Example:
     * <pre>
     * stopPredicate: (index, value) -&gt; (index + value) &gt; 4
     * stream: [1, 2, 3, 4, 0, 1, 2]
     * index:  [0, 1, 2, 3, 4, 5, 6]
     * sum:    [1, 3, 5, 7, 4, 6, 8]
     * result: [1, 2, 3]
     * </pre>
     *
     * @param stopPredicate  the {@code IndexedPredicate} used to take elements
     * @return the new stream
     * @since 1.1.6
     */
    public Stream<T> takeUntilIndexed(IndexedPredicate<? super T> stopPredicate) {
        return takeUntilIndexed(0, 1, stopPredicate);
    }

    /**
     * Takes elements while the {@code IndexedPredicate} returns {@code false}.
     * Once predicate condition is satisfied by an element, the stream
     * finishes with this element.
     *
     * <p>This is an intermediate operation.
     *
     * <p>Example:
     * <pre>
     * from: 2
     * step: 2
     * stopPredicate: (index, value) -&gt; (index + value) &gt; 8
     * stream: [1, 2, 3,  4,  0,  1,  2]
     * index:  [2, 4, 6,  8, 10, 11, 14]
     * sum:    [3, 6, 9, 12, 10, 12, 16]
     * result: [1, 2, 3]
     * </pre>
     *
     * @param from  the initial value of the index (inclusive)
     * @param step  the step of the index
     * @param stopPredicate  the {@code IndexedPredicate} used to take elements
     * @return the new stream
     * @since 1.1.6
     */
    public Stream<T> takeUntilIndexed(int from, int step, IndexedPredicate<? super T> stopPredicate) {
        return new Stream<T>(params, new ObjTakeUntilIndexed<T>(
                new IndexedIterator<T>(from, step, iterator),
                stopPredicate));
    }

    /**
     * Drops elements while the predicate is true, then returns the rest.
     *
     * <p>This is an intermediate operation.
     *
     * <p>Example:
     * <pre>
     * predicate: (a) -&gt; a &lt; 3
     * stream: [1, 2, 3, 4, 1, 2, 3, 4]
     * result: [3, 4, 1, 2, 3, 4]
     * </pre>
     *
     * @param predicate  the predicate used to drop elements
     * @return the new stream
     */
    public Stream<T> dropWhile(final Predicate<? super T> predicate) {
        return new Stream<T>(params, new ObjDropWhile<T>(iterator, predicate));
    }

    /**
     * Drops elements while the {@code IndexedPredicate} is true, then returns the rest.
     *
     * <p>This is an intermediate operation.
     *
     * <p>Example:
     * <pre>
     * predicate: (index, value) -&gt; (index + value) &lt; 5
     * stream: [1, 2, 3, 4, 0, 1, 2]
     * index:  [0, 1, 2, 3, 4, 5, 6]
     * sum:    [1, 3, 5, 7, 4, 6, 8]
     * result: [3, 4, 0, 1, 2]
     * </pre>
     *
     * @param predicate  the {@code IndexedPredicate} used to drop elements
     * @return the new stream
     * @since 1.1.6
     */
    public Stream<T> dropWhileIndexed(IndexedPredicate<? super T> predicate) {
        return dropWhileIndexed(0, 1, predicate);
    }

    /**
     * Drops elements while the {@code IndexedPredicate} is true, then returns the rest.
     *
     * <p>This is an intermediate operation.
     *
     * <p>Example:
     * <pre>
     * from: 2
     * step: 2
     * predicate: (index, value) -&gt; (index + value) &lt; 10
     * stream: [1, 2, 3,  4, -5, -6, -7]
     * index:  [2, 4, 6,  8, 10, 12, 14]
     * sum:    [3, 6, 9, 12,  5,  6,  7]
     * result: [4, -5, -6, -7]
     * </pre>
     *
     * @param from  the initial value of the index (inclusive)
     * @param step  the step of the index
     * @param predicate  the {@code IndexedPredicate} used to drop elements
     * @return the new stream
     * @since 1.1.6
     */
    public Stream<T> dropWhileIndexed(int from, int step, IndexedPredicate<? super T> predicate) {
        return new Stream<T>(params, new ObjDropWhileIndexed<T>(
                new IndexedIterator<T>(from, step, iterator),
                predicate));
    }

    /**
     * Returns {@code Stream} with first {@code maxSize} elements.
     *
     * <p>This is a short-circuiting stateful intermediate operation.
     *
     * <p>Example:
     * <pre>
     * maxSize: 3
     * stream: [1, 2, 3, 4, 5]
     * result: [1, 2, 3]
     *
     * maxSize: 10
     * stream: [1, 2]
     * result: [1, 2]
     * </pre>
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
        return new Stream<T>(params, new ObjLimit<T>(iterator, maxSize));
    }

    /**
     * Skips first {@code n} elements and returns {@code Stream} with remaining elements.
     * If stream contains fewer than {@code n} elements, then an empty stream will be returned.
     *
     * <p>This is a stateful intermediate operation.
     *
     * <p>Example:
     * <pre>
     * n: 3
     * stream: [1, 2, 3, 4, 5]
     * result: [4, 5]
     *
     * n: 10
     * stream: [1, 2]
     * result: []
     * </pre>
     *
     * @param n  the number of elements to skip
     * @return the new stream
     * @throws IllegalArgumentException if {@code n} is negative
     */
    public Stream<T> skip(final long n) {
        if (n < 0) throw new IllegalArgumentException("n cannot be negative");
        if (n == 0) return this;
        return new Stream<T>(params, new ObjSkip<T>(iterator, n));
    }

    /**
     * Performs the given action on each element.
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
     * Performs the given indexed action on each element.
     *
     * <p>This is a terminal operation.
     *
     * @param action  the action to be performed on each element
     * @since 1.1.6
     */
    public void forEachIndexed(IndexedConsumer<? super T> action) {
        forEachIndexed(0, 1, action);
    }

    /**
     * Performs the given indexed action on each element.
     *
     * <p>This is a terminal operation.
     *
     * @param from  the initial value of the index (inclusive)
     * @param step  the step of the index
     * @param action  the action to be performed on each element
     * @since 1.1.6
     */
    public void forEachIndexed(int from, int step, IndexedConsumer<? super T> action) {
        int index = from;
        while (iterator.hasNext()) {
            action.accept(index, iterator.next());
            index += step;
        }
    }

    /**
     * Reduces the elements using provided identity value and the associative accumulation function.
     *
     * <p>This is a terminal operation.
     *
     * <p>Example:
     * <pre>
     * identity: 0
     * accumulator: (a, b) -&gt; a + b
     * stream: [1, 2, 3, 4, 5]
     * result: 15
     * </pre>
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
     * Reduces the elements using provided identity value and
     * the associative accumulation indexed function.
     *
     * <p>This is a terminal operation.
     *
     * <p>Example:
     * <pre>
     * identity: 10
     * accumulator: (index, a, b) -&gt; index + a + b
     * stream: [1, 2, 3, 4, 5]
     * index:  [0, 1, 2, 3, 4]
     * result: 10 + 1 + 3 + 5 + 7 + 9 = 35
     * </pre>
     *
     * @param <R> the type of the result
     * @param identity  the initial value
     * @param accumulator  the accumulation function
     * @return the result of the reduction
     * @since 1.1.6
     */
    public <R> R reduceIndexed(R identity, IndexedBiFunction<? super R, ? super T, ? extends R> accumulator) {
        return reduceIndexed(0, 1, identity, accumulator);
    }

    /**
     * Reduces the elements using provided identity value and
     * the associative accumulation indexed function.
     *
     * <p>This is a terminal operation.
     *
     * <p>Example:
     * <pre>
     * from: 1
     * step: 2
     * identity: 10
     * accumulator: (index, a, b) -&gt; index + a + b
     * stream: [1, 2, 3, 4, 5]
     * index:  [1, 3, 5, 7, 9]
     * result: 10 + 2 + 5 + 8 + 11 + 14 = 50
     * </pre>
     *
     * @param <R> the type of the result
     * @param from  the initial value of the index (inclusive)
     * @param step  the step of the index
     * @param identity  the initial value
     * @param accumulator  the accumulation function
     * @return the result of the reduction
     * @since 1.1.6
     */
    public <R> R reduceIndexed(int from, int step, R identity,
            IndexedBiFunction<? super R, ? super T, ? extends R> accumulator) {
        R result = identity;
        int index = from;
        while (iterator.hasNext()) {
            final T value = iterator.next();
            result = accumulator.apply(index, result, value);
            index += step;
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
     * @see #reduce(java.lang.Object, com.annimon.stream.function.BiFunction)
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
    public <R> R[] toArray(IntFunction<R[]> generator) {
        return Operators.toArray(iterator, generator);
    }

    /**
     * Collects elements to a new {@code List}.
     *
     * <p>This implementation <strong>does not</strong> call {@code collect(Collectors.toList())}, so
     * it can be faster by reducing method calls.
     *
     * <p>This is a terminal operation.
     *
     * @return a new {@code List}
     * @since 1.1.5
     * @see Collectors#toList()
     */
    public List<T> toList() {
        final List<T> result = new ArrayList<T>();
        while (iterator.hasNext()) {
            result.add(iterator.next());
        }
        return result;
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
        final R result = supplier.get();
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
     * <p>Example:
     * <pre>
     * comparator: (a, b) -&gt; a.compareTo(b)
     * stream: [1, 2, 3, 4, 5]
     * result: 1
     * </pre>
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
     * <p>Example:
     * <pre>
     * comparator: (a, b) -&gt; a.compareTo(b)
     * stream: [1, 2, 3, 4, 5]
     * result: 5
     * </pre>
     *
     * @param comparator  the {@code Comparator} to compare elements
     * @return the maximum element
     */
    public Optional<T> max(Comparator<? super T> comparator) {
        return reduce(BinaryOperator.Util.<T>maxBy(comparator));
    }

    /**
     * Returns the count of elements in this stream.
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
     * <p>Example:
     * <pre>
     * predicate: (a) -&gt; a == 5
     * stream: [1, 2, 3, 4, 5]
     * result: true
     *
     * predicate: (a) -&gt; a == 5
     * stream: [5, 5, 5]
     * result: true
     * </pre>
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
     * <p>Example:
     * <pre>
     * predicate: (a) -&gt; a == 5
     * stream: [1, 2, 3, 4, 5]
     * result: false
     *
     * predicate: (a) -&gt; a == 5
     * stream: [5, 5, 5]
     * result: true
     * </pre>
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
     * <p>Example:
     * <pre>
     * predicate: (a) -&gt; a == 5
     * stream: [1, 2, 3, 4, 5]
     * result: false
     *
     * predicate: (a) -&gt; a == 5
     * stream: [1, 2, 3]
     * result: true
     * </pre>
     *
     * @param predicate  the predicate used to match elements
     * @return {@code true} if no elements match the given predicate, otherwise {@code false}
     */
    public boolean noneMatch(Predicate<? super T> predicate) {
        return match(predicate, MATCH_NONE);
    }

    /**
     * Finds the first element and its index that matches the given predicate.
     *
     * <p>This is a short-circuiting terminal operation.
     *
     * <p>Example:
     * <pre>
     * predicate: (index, value) -&gt; index + value == 7
     * stream: [1, 2, 3, 4, 5, 2, 0]
     * index:  [0, 1, 2, 3, 4, 5, 6]
     * result: Optional.of(IntPair(3, 4))
     * </pre>
     *
     * @param predicate  the predicate to find value
     * @return an {@code Optional} with {@code IntPair}
     *         or {@code Optional.empty()} if stream is empty or no value was found.
     * @since 1.1.8
     */
    public Optional<IntPair<T>> findIndexed(IndexedPredicate<? super T> predicate) {
        return findIndexed(0, 1, predicate);
    }

    /**
     * Finds the first element and its index that matches the given predicate.
     *
     * <p>This is a short-circuiting terminal operation.
     *
     * <p>Example:
     * <pre>
     * from: 0
     * step: 10
     * predicate: (index, value) -&gt; index + value == 42
     * stream: [1, 11, 22, 12, 40]
     * index:  [0, 10, 20, 30, 40]
     * result: Optional.of(IntPair(20, 22))
     * </pre>
     *
     * @param from  the initial value of the index (inclusive)
     * @param step  the step of the index
     * @param predicate  the predicate to find value
     * @return an {@code Optional} with {@code IntPair}
     *         or {@code Optional.empty()} if stream is empty or no value was found.
     * @since 1.1.8
     */
    public Optional<IntPair<T>> findIndexed(int from, int step,
                                            IndexedPredicate<? super T> predicate) {
        int index = from;
        while (iterator.hasNext()) {
            final T value = iterator.next();
            if (predicate.test(index, value)) {
                return Optional.of(new IntPair<T>(index, value));
            }
            index += step;
        }
        return Optional.empty();
    }

    /**
     * Returns the first element wrapped by {@code Optional} class.
     * If stream is empty, returns {@code Optional.empty()}.
     *
     * <p>This is a short-circuiting terminal operation.
     *
     * @return an {@code Optional} with the first element
     *         or {@code Optional.empty()} if stream is empty
     */
    public Optional<T> findFirst() {
        if (iterator.hasNext()) {
            return Optional.of(iterator.next());
        }
        return Optional.empty();
    }

    /**
     * Returns the last element wrapped by {@code Optional} class.
     * If stream is empty, returns {@code Optional.empty()}.
     *
     * <p>This is a short-circuiting terminal operation.
     *
     * @return an {@code Optional} with the last element
     *         or {@code Optional.empty()} if the stream is empty
     * @since 1.1.8
     */
    public Optional<T> findLast() {
        return reduce(new BinaryOperator<T>() {
            @Override
            public T apply(T left, T right) {
                return right;
            }
        });
    }

    /**
     * Returns the single element of stream.
     * If stream is empty, throws {@code NoSuchElementException}.
     * If stream contains more than one element, throws {@code IllegalStateException}.
     *
     * <p>This is a short-circuiting terminal operation.
     *
     * <p>Example:
     * <pre>
     * stream: []
     * result: NoSuchElementException
     *
     * stream: [1]
     * result: 1
     *
     * stream: [1, 2, 3]
     * result: IllegalStateException
     * </pre>
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
     * <p>Example:
     * <pre>
     * stream: []
     * result: Optional.empty()
     *
     * stream: [1]
     * result: Optional.of(1)
     *
     * stream: [1, 2, 3]
     * result: IllegalStateException
     * </pre>
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

    /**
     * Adds close handler to the current stream.
     *
     * <p>This is an intermediate operation.
     *
     * @param closeHandler  an action to execute when the stream is closed
     * @return the new stream with the close handler
     * @since 1.1.8
     */
    public Stream<T> onClose(final Runnable closeHandler) {
        Objects.requireNonNull(closeHandler);
        final Params newParams;
        if (params == null) {
            newParams = new Params();
            newParams.closeHandler = closeHandler;
        } else {
            newParams = params;
            final Runnable firstHandler = newParams.closeHandler;
            newParams.closeHandler = Compose.runnables(firstHandler, closeHandler);
        }
        return new Stream<T>(newParams, iterator);
    }

    /**
     * Causes close handler to be invoked if it exists.
     * Since most of the stream providers are lists or arrays,
     * it is not necessary to close the stream.
     *
     * @since 1.1.8
     */
    @Override
    public void close() {
        if (params != null && params.closeHandler != null) {
            params.closeHandler.run();
            params.closeHandler = null;
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
//</editor-fold>
}
