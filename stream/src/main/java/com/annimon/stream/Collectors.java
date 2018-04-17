package com.annimon.stream;

import com.annimon.stream.function.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Common implementations of {@code Collector} interface.
 *
 * @see Collector
 */
public final class Collectors {

    private static final Supplier<long[]> LONG_2ELEMENTS_ARRAY_SUPPLIER = new Supplier<long[]>() {
        @Override
        public long[] get() {
            return new long[] { 0L, 0L };
        }
    };

    private static final Supplier<double[]> DOUBLE_2ELEMENTS_ARRAY_SUPPLIER = new Supplier<double[]>() {
        @Override
        public double[] get() {
            return new double[] { 0d, 0d };
        }
    };

    private Collectors() { }

    /**
     * Returns a {@code Collector} that fills new {@code Collection}, provided by {@code collectionSupplier},
     * with input elements.
     *
     * @param <T> the type of the input elements
     * @param <R> the type of the resulting collection
     * @param collectionSupplier  a supplier function that provides new collection
     * @return a {@code Collector}
     */
    public static <T, R extends Collection<T>> Collector<T, ?, R> toCollection(Supplier<R> collectionSupplier) {
        return new CollectorsImpl<T, R, R>(

                collectionSupplier,

                new BiConsumer<R, T>() {
                    @Override
                    public void accept(R t, T u) {
                        t.add(u);
                    }
                }
        );
    }

    /**
     * Returns a {@code Collector} that fills new {@code List} with input elements.
     *
     * @param <T> the type of the input elements
     * @return a {@code Collector}
     */
    public static <T> Collector<T, ?, List<T>> toList() {
        return new CollectorsImpl<T, List<T>, List<T>>(

                new Supplier<List<T>>() {
                    @Override
                    public List<T> get() {
                        return new ArrayList<T>();
                    }
                },

                new BiConsumer<List<T>, T>() {
                    @Override
                    public void accept(List<T> t, T u) {
                        t.add(u);
                    }
                }
        );
    }

    /**
     * Returns a {@code Collector} that fills new unmodifiable {@code List} with input elements.
     *
     * The returned {@code Collector} disallows {@code null}s
     * and throws {@code NullPointerException} if it is presented with a null value.
     *
     * @param <T> the type of the input elements
     * @return a {@code Collector}
     * @since 1.2.0
     */
    public static <T> Collector<T, ?, List<T>> toUnmodifiableList() {
        return Collectors.collectingAndThen(Collectors.<T>toList(), new UnaryOperator<List<T>>() {

            @Override
            public List<T> apply(List<T> list) {
                Objects.requireNonNullElements(list);
                return Collections.unmodifiableList(list);
            }
        });
    }

    /**
     * Returns a {@code Collector} that fills new {@code Set} with input elements.
     *
     * @param <T> the type of the input elements
     * @return a {@code Collector}
     */
    public static <T> Collector<T, ?, Set<T>> toSet() {
        return new CollectorsImpl<T, Set<T>, Set<T>>(

                new Supplier<Set<T>>() {
                    @Override
                    public Set<T> get() {
                        return new HashSet<T>();
                    }
                },

                new BiConsumer<Set<T>, T>() {
                    @Override
                    public void accept(Set<T> set, T t) {
                        set.add(t);
                    }
                }
        );
    }

    /**
     * Returns a {@code Collector} that fills new unmodifiable {@code Set} with input elements.
     *
     * The returned {@code Collector} disallows {@code null}s
     * and throws {@code NullPointerException} if it is presented with a null value.
     * If elements contain duplicates, an arbitrary element of the duplicates is preserved.
     *
     * @param <T> the type of the input elements
     * @return a {@code Collector}
     * @since 1.2.0
     */
    public static <T> Collector<T, ?, Set<T>> toUnmodifiableSet() {
        return Collectors.collectingAndThen(Collectors.<T>toSet(), new UnaryOperator<Set<T>>() {

            @Override
            public Set<T> apply(Set<T> set) {
                Objects.requireNonNullElements(set);
                return Collections.unmodifiableSet(set);
            }
        });
    }

    /**
     * Returns a {@code Collector} that fills new {@code Map} with input elements.
     *
     * If the mapped keys contain duplicates, an {@code IllegalStateException} is thrown.
     * Use {@link #toMap(Function, Function, BinaryOperator)} to handle merging of the values.
     *
     * @param <T> the type of the input elements and the result type of value mapping function
     * @param <K> the result type of key mapping function
     * @param keyMapper  a mapping function to produce keys
     * @return a {@code Collector}
     * @since 1.1.3
     * @see #toMap(Function, Function, BinaryOperator)
     */
    public static <T, K> Collector<T, ?, Map<K, T>> toMap(
            final Function<? super T, ? extends K> keyMapper) {
        return Collectors.<T, K, T>toMap(keyMapper, UnaryOperator.Util.<T>identity());
    }

    /**
     * Returns a {@code Collector} that fills new {@code Map} with input elements.
     *
     * If the mapped keys contain duplicates, an {@code IllegalStateException} is thrown.
     * Use {@link #toMap(Function, Function, BinaryOperator)} to handle merging of the values.
     *
     * @param <T> the type of the input elements
     * @param <K> the result type of key mapping function
     * @param <V> the result type of value mapping function
     * @param keyMapper  a mapping function to produce keys
     * @param valueMapper  a mapping function to produce values
     * @return a {@code Collector}
     * @see #toMap(Function, Function, BinaryOperator)
     */
    public static <T, K, V> Collector<T, ?, Map<K, V>> toMap(
            final Function<? super T, ? extends K> keyMapper,
            final Function<? super T, ? extends V> valueMapper) {
        return Collectors.<T, K, V, Map<K, V>>toMap(keyMapper, valueMapper,
                Collectors.<K, V>hashMapSupplier());
    }

    /**
     * Returns a {@code Collector} that fills new {@code Map} with input elements.
     *
     * If the mapped keys contain duplicates, an {@code IllegalStateException} is thrown.
     * Use {@link #toMap(Function, Function, BinaryOperator, Supplier)} to handle merging of the values.
     *
     * @param <T> the type of the input elements
     * @param <K> the result type of key mapping function
     * @param <V> the result type of value mapping function
     * @param <M> the type of the resulting {@code Map}
     * @param keyMapper  a mapping function to produce keys
     * @param valueMapper  a mapping function to produce values
     * @param mapFactory  a supplier function that provides new {@code Map}
     * @return a {@code Collector}
     * @see #toMap(Function, Function, BinaryOperator, Supplier)
     */
    public static <T, K, V, M extends Map<K, V>> Collector<T, ?, M> toMap(
            final Function<? super T, ? extends K> keyMapper,
            final Function<? super T, ? extends V> valueMapper,
            final Supplier<M> mapFactory) {
        return new CollectorsImpl<T, M, M>(

                mapFactory,

                new BiConsumer<M, T>() {
                    @Override
                    public void accept(M map, T t) {
                        final K key = keyMapper.apply(t);
                        final V value = Objects.requireNonNull(valueMapper.apply(t));

                        // To avoid calling map.get to determine duplicate keys
                        // we check the result of map.put
                        final V oldValue = map.put(key, value);
                        if (oldValue != null) {
                            // If there is duplicate key, rollback previous put operation
                            map.put(key, oldValue);
                            throw duplicateKeyException(key, oldValue, value);
                        }
                    }
                }
        );
    }

    /**
     * Returns a {@code Collector} that fills new unmodifiable {@code Map} with input elements.
     *
     * The returned {@code Collector} disallows {@code null} keys and values.
     * If the mapped keys contain duplicates, an {@code IllegalStateException} is thrown,
     * see {@link #toUnmodifiableMap(Function, Function, BinaryOperator)}.
     *
     * @param <T> the type of the input elements
     * @param <K> the result type of key mapping function
     * @param <V> the result type of value mapping function
     * @param keyMapper  a mapping function to produce keys
     * @param valueMapper  a mapping function to produce values
     * @return a {@code Collector}
     * @see #toUnmodifiableMap(Function, Function, BinaryOperator)
     * @since 1.2.0
     */
    public static <T, K, V> Collector<T, ?, Map<K, V>> toUnmodifiableMap(
            final Function<? super T, ? extends K> keyMapper,
            final Function<? super T, ? extends V> valueMapper) {
        return Collectors.collectingAndThen(
                Collectors.<T, K, V>toMap(keyMapper, valueMapper),
                Collectors.<K, V>toUnmodifiableMapConverter());
    }

    /**
     * Returns a {@code Collector} that fills new {@code Map} with input elements.
     *
     * If the mapped keys contain duplicates, the value mapping function is applied
     * to each equal element, and the results are merged using the provided merging function.
     *
     * @param <T> the type of the input elements
     * @param <K> the result type of key mapping function
     * @param <V> the result type of value mapping function
     * @param keyMapper  a mapping function to produce keys
     * @param valueMapper  a mapping function to produce values
     * @param mergeFunction  a merge function, used to resolve collisions between
     *                       values associated with the same key
     * @return a {@code Collector}
     * @since 1.2.0
     */
    public static <T, K, V> Collector<T, ?, Map<K, V>> toMap(
            final Function<? super T, ? extends K> keyMapper,
            final Function<? super T, ? extends V> valueMapper,
            final BinaryOperator<V> mergeFunction) {
        return Collectors.<T, K, V, Map<K, V>>toMap(
                keyMapper, valueMapper, mergeFunction,
                Collectors.<K, V>hashMapSupplier());
    }

    /**
     * Returns a {@code Collector} that fills new {@code Map} with input elements.
     *
     * If the mapped keys contain duplicates, the value mapping function is applied
     * to each equal element, and the results are merged using the provided merging function.
     *
     * @param <T> the type of the input elements
     * @param <K> the result type of key mapping function
     * @param <V> the result type of value mapping function
     * @param <M> the type of the resulting {@code Map}
     * @param keyMapper  a mapping function to produce keys
     * @param valueMapper  a mapping function to produce values
     * @param mergeFunction  a merge function, used to resolve collisions between
     *                       values associated with the same key
     * @param mapFactory  a supplier function that provides new {@code Map}
     * @return a {@code Collector}
     * @since 1.2.0
     */
    public static <T, K, V, M extends Map<K, V>> Collector<T, ?, M> toMap(
            final Function<? super T, ? extends K> keyMapper,
            final Function<? super T, ? extends V> valueMapper,
            final BinaryOperator<V> mergeFunction,
            final Supplier<M> mapFactory) {
        return new CollectorsImpl<T, M, M>(

                mapFactory,

                new BiConsumer<M, T>() {
                    @Override
                    public void accept(M map, T t) {
                        final K key = keyMapper.apply(t);
                        final V value = valueMapper.apply(t);
                        mapMerge(map, key, value, mergeFunction);
                    }
                }
        );
    }

    /**
     * Returns a {@code Collector} that fills new unmodifiable {@code Map} with input elements.
     *
     * The returned {@code Collector} disallows {@code null} keys and values.
     *
     * @param <T> the type of the input elements
     * @param <K> the result type of key mapping function
     * @param <V> the result type of value mapping function
     * @param keyMapper  a mapping function to produce keys
     * @param valueMapper  a mapping function to produce values
     * @param mergeFunction  a merge function, used to resolve collisions between
     *                       values associated with the same key
     * @return a {@code Collector}
     * @since 1.2.0
     */
    public static <T, K, V> Collector<T, ?, Map<K, V>> toUnmodifiableMap(
            final Function<? super T, ? extends K> keyMapper,
            final Function<? super T, ? extends V> valueMapper,
            final BinaryOperator<V> mergeFunction) {
        return Collectors.collectingAndThen(
                Collectors.<T, K, V, Map<K, V>>toMap(
                        keyMapper, valueMapper, mergeFunction,
                        Collectors.<K, V>hashMapSupplier()),
                Collectors.<K, V>toUnmodifiableMapConverter());
    }

    /**
     * Returns a {@code Collector} that concatenates input elements into new string.
     *
     * @return a {@code Collector}
     */
    public static Collector<CharSequence, ?, String> joining() {
        return joining("");
    }

    /**
     * Returns a {@code Collector} that concatenates input elements into new string.
     *
     * @param delimiter  the delimiter between each element
     * @return a {@code Collector}
     */
    public static Collector<CharSequence, ?, String> joining(CharSequence delimiter) {
        return joining(delimiter, "", "");
    }

    /**
     * Returns a {@code Collector} that concatenates input elements into new string.
     *
     * @param delimiter  the delimiter between each element
     * @param prefix  the prefix of result
     * @param suffix  the suffix of result
     * @return a {@code Collector}
     */
    public static Collector<CharSequence, ?, String> joining(CharSequence delimiter, CharSequence prefix, CharSequence suffix) {
        return joining(delimiter, prefix, suffix, prefix.toString() + suffix.toString());
    }

    /**
     * Returns a {@code Collector} that concatenates input elements into new string.
     *
     * @param delimiter  the delimiter between each element
     * @param prefix  the prefix of result
     * @param suffix  the suffix of result
     * @param emptyValue  the string which replaces empty element if exists
     * @return a {@code Collector}
     */
    public static Collector<CharSequence, ?, String> joining(
            final CharSequence delimiter,
            final CharSequence prefix,
            final CharSequence suffix,
            final String emptyValue) {
        return new CollectorsImpl<CharSequence, StringBuilder, String>(

                new Supplier<StringBuilder>() {
                    @Override
                    public StringBuilder get() {
                        return new StringBuilder();
                    }
                },

                new BiConsumer<StringBuilder, CharSequence>() {
                    @Override
                    public void accept(StringBuilder t, CharSequence u) {
                        if (t.length() > 0) {
                            t.append(delimiter);
                        } else {
                            t.append(prefix);
                        }
                        t.append(u);
                    }
                },

                new Function<StringBuilder, String>() {
                    @Override
                    public String apply(StringBuilder value) {
                        if (value.length() == 0) {
                            return emptyValue;
                        } else {
                            value.append(suffix);
                            return value.toString();
                        }
                    }
                }
        );
    }

    /**
     * Returns a {@code Collector} that calculates average of input elements.
     *
     * @param <T> the type of the input elements
     * @param mapper  the mapping function which extracts value from element to calculate result
     * @deprecated  As of release 1.1.3, replaced by
     *              {@link #averagingDouble(com.annimon.stream.function.ToDoubleFunction)}
     * @return a {@code Collector}
     */
    @Deprecated
    public static <T> Collector<T, ?, Double> averaging(final Function<? super T, Double> mapper) {
        return averagingDouble(new ToDoubleFunction<T>() {

            @Override
            public double applyAsDouble(T t) {
                return mapper.apply(t);
            }
        });
    }

    /**
     * Returns a {@code Collector} that calculates average of integer-valued input elements.
     *
     * @param <T> the type of the input elements
     * @param mapper  the mapping function which extracts value from element to calculate result
     * @return a {@code Collector}
     * @since 1.1.3
     */
    public static <T> Collector<T, ?, Double> averagingInt(final ToIntFunction<? super T> mapper) {
        return averagingHelper(new BiConsumer<long[], T>() {
            @Override
            public void accept(long[] t, T u) {
                t[0]++; // count
                t[1] += mapper.applyAsInt(u); // sum
            }
        });
    }

    /**
     * Returns a {@code Collector} that calculates average of long-valued input elements.
     *
     * @param <T> the type of the input elements
     * @param mapper  the mapping function which extracts value from element to calculate result
     * @return a {@code Collector}
     * @since 1.1.3
     */
    public static <T> Collector<T, ?, Double> averagingLong(final ToLongFunction<? super T> mapper) {
        return averagingHelper(new BiConsumer<long[], T>() {
            @Override
            public void accept(long[] t, T u) {
                t[0]++; // count
                t[1] += mapper.applyAsLong(u); // sum
            }
        });
    }

    private static <T> Collector<T, ?, Double> averagingHelper(final BiConsumer<long[], T> accumulator) {
        return new CollectorsImpl<T, long[], Double>(

                LONG_2ELEMENTS_ARRAY_SUPPLIER,

                accumulator,

                new Function<long[], Double>() {
                    @Override
                    public Double apply(long[] t) {
                        if (t[0] == 0) return 0d;
                        return t[1] / (double) t[0];
                    }
                }
        );
    }

    /**
     * Returns a {@code Collector} that calculates average of double-valued input elements.
     *
     * @param <T> the type of the input elements
     * @param mapper  the mapping function which extracts value from element to calculate result
     * @return a {@code Collector}
     * @since 1.1.3
     */
    public static <T> Collector<T, ?, Double> averagingDouble(final ToDoubleFunction<? super T> mapper) {
        return new CollectorsImpl<T, double[], Double>(

                DOUBLE_2ELEMENTS_ARRAY_SUPPLIER,

                new BiConsumer<double[], T>() {
                    @Override
                    public void accept(double[] t, T u) {
                        t[0]++; // count
                        t[1] += mapper.applyAsDouble(u); // sum
                    }
                },

                new Function<double[], Double>() {
                    @Override
                    public Double apply(double[] t) {
                        if (t[0] == 0) return 0d;
                        return t[1] / t[0];
                    }
                }
        );
    }

    /**
     * Returns a {@code Collector} that summing integer-valued input elements.
     *
     * @param <T> the type of the input elements
     * @param mapper  the mapping function which extracts value from element to calculate result
     * @return a {@code Collector}
     * @since 1.1.3
     */
    public static <T> Collector<T, ?, Integer> summingInt(final ToIntFunction<? super T> mapper) {
        return new CollectorsImpl<T, int[], Integer>(

                new Supplier<int[]>() {
                    @Override
                    public int[] get() {
                        return new int[] { 0 };
                    }
                },

                new BiConsumer<int[], T>() {
                    @Override
                    public void accept(int[] t, T u) {
                        t[0] += mapper.applyAsInt(u);
                    }
                },

                new Function<int[], Integer>() {
                    @Override
                    public Integer apply(int[] value) {
                        return value[0];
                    }
                }
        );
    }

    /**
     * Returns a {@code Collector} that summing long-valued input elements.
     *
     * @param <T> the type of the input elements
     * @param mapper  the mapping function which extracts value from element to calculate result
     * @return a {@code Collector}
     * @since 1.1.3
     */
    public static <T> Collector<T, ?, Long> summingLong(final ToLongFunction<? super T> mapper) {
        return new CollectorsImpl<T, long[], Long>(

                LONG_2ELEMENTS_ARRAY_SUPPLIER,

                new BiConsumer<long[], T>() {
                    @Override
                    public void accept(long[] t, T u) {
                        t[0] += mapper.applyAsLong(u);
                    }
                },

                new Function<long[], Long>() {
                    @Override
                    public Long apply(long[] value) {
                        return value[0];
                    }
                }
        );
    }

    /**
     * Returns a {@code Collector} that summing double-valued input elements.
     *
     * @param <T> the type of the input elements
     * @param mapper  the mapping function which extracts value from element to calculate result
     * @return a {@code Collector}
     * @since 1.1.3
     */
    public static <T> Collector<T, ?, Double> summingDouble(final ToDoubleFunction<? super T> mapper) {
        return new CollectorsImpl<T, double[], Double>(

                DOUBLE_2ELEMENTS_ARRAY_SUPPLIER,

                new BiConsumer<double[], T>() {
                    @Override
                    public void accept(double[] t, T u) {
                        t[0] += mapper.applyAsDouble(u);
                    }
                },

                new Function<double[], Double>() {
                    @Override
                    public Double apply(double[] value) {
                        return value[0];
                    }
                }
        );
    }

    /**
     * Returns a {@code Collector} that counts the number of input elements.
     *
     * @param <T> the type of the input elements
     * @return a {@code Collector}
     */
    public static <T> Collector<T, ?, Long> counting() {
        return summingLong(new ToLongFunction<T>() {

            @Override
            public long applyAsLong(T t) {
                return 1L;
            }
        });
    }

    /**
     * Returns a {@code Collector} that reduces input elements.
     *
     * @param <T> the type of the input elements
     * @param identity  the initial value
     * @param op  the operator to reduce elements
     * @return a {@code Collector}
     * @see #reducing(java.lang.Object, com.annimon.stream.function.Function, com.annimon.stream.function.BinaryOperator)
     */
    public static <T> Collector<T, ?, T> reducing(final T identity, final BinaryOperator<T> op) {
        return new CollectorsImpl<T, Tuple1<T>, T>(

                new Supplier<Tuple1<T>>() {
                    @Override
                    public Tuple1<T> get() {
                        return new Tuple1<T>(identity);
                    }
                },

                new BiConsumer<Tuple1<T>, T>() {
                    @Override
                    public void accept(Tuple1<T> tuple, T value) {
                        tuple.a = op.apply(tuple.a, value);
                    }
                },

                new Function<Tuple1<T>, T>() {
                    @Override
                    public T apply(Tuple1<T> tuple) {
                        return tuple.a;
                    }
                }
        );
    }

    /**
     * Returns a {@code Collector} that reduces input elements.
     *
     * @param <T> the type of the input elements
     * @param <R> the type of the output elements
     * @param identity  the initial value
     * @param mapper  the mapping function
     * @param op  the operator to reduce elements
     * @return a {@code Collector}
     * @see #reducing(java.lang.Object, com.annimon.stream.function.BinaryOperator)
     */
    public static <T, R> Collector<T, ?, R> reducing(
            final R identity,
            final Function<? super T, ? extends R> mapper,
            final BinaryOperator<R> op) {
        return new CollectorsImpl<T, Tuple1<R>, R>(

                new Supplier<Tuple1<R>>() {
                    @Override
                    public Tuple1<R> get() {
                        return new Tuple1<R>(identity);
                    }
                },

                new BiConsumer<Tuple1<R>, T>() {
                    @Override
                    public void accept(Tuple1<R> tuple, T value) {
                        tuple.a = op.apply(tuple.a, mapper.apply(value));
                    }
                },

                new Function<Tuple1<R>, R>() {
                    @Override
                    public R apply(Tuple1<R> tuple) {
                        return tuple.a;
                    }
                }
        );
    }

    /**
     * Returns a {@code Collector} that filters input elements.
     *
     * @param <T> the type of the input elements
     * @param <A> the accumulation type
     * @param <R> the type of the output elements
     * @param predicate  a predicate used to filter elements
     * @param downstream  the collector of filtered elements
     * @return a {@code Collector}
     * @since 1.1.3
     */
    public static <T, A, R> Collector<T, ?, R> filtering(
            final Predicate<? super T> predicate,
            final Collector<? super T, A, R> downstream) {
        final BiConsumer<A, ? super T> accumulator = downstream.accumulator();
        return new CollectorsImpl<T, A, R>(

                downstream.supplier(),

                new BiConsumer<A, T>() {
                    @Override
                    public void accept(A a, T t) {
                        if (predicate.test(t))
                            accumulator.accept(a, t);
                    }
                },

                downstream.finisher()
        );
    }

    /**
     * Returns a {@code Collector} that performs mapping before accumulation.
     *
     * @param <T> the type of the input elements
     * @param <U> the result type of mapping function
     * @param <A> the accumulation type
     * @param <R> the result type of collector
     * @param mapper  a function that performs mapping to input elements
     * @param downstream  the collector of mapped elements
     * @return a {@code Collector}
     */
    public static <T, U, A, R> Collector<T, ?, R> mapping(
            final Function<? super T, ? extends U> mapper,
            final Collector<? super U, A, R> downstream) {

        final BiConsumer<A, ? super U> accumulator = downstream.accumulator();
        return new CollectorsImpl<T, A, R>(

                downstream.supplier(),

                new BiConsumer<A, T>() {
                    @Override
                    public void accept(A a, T t) {
                        accumulator.accept(a, mapper.apply(t));
                    }
                },

                downstream.finisher()
        );
    }

    /**
     * Returns a {@code Collector} that performs flat-mapping before accumulation.
     *
     * @param <T> the type of the input elements
     * @param <U> the result type of flat-mapping function
     * @param <A> the accumulation type
     * @param <R> the result type of collector
     * @param mapper  a function that performs flat-mapping to input elements
     * @param downstream  the collector of flat-mapped elements
     * @return a {@code Collector}
     * @since 1.1.3
     */
    public static <T, U, A, R> Collector<T, ?, R> flatMapping(
            final Function<? super T, ? extends Stream<? extends U>> mapper,
            final Collector<? super U, A, R> downstream) {

        final BiConsumer<A, ? super U> accumulator = downstream.accumulator();
        return new CollectorsImpl<T, A, R>(

                downstream.supplier(),

                new BiConsumer<A, T>() {
                    @Override
                    public void accept(final A a, T t) {
                        final Stream<? extends U> stream = mapper.apply(t);
                        if (stream == null) return;
                        stream.forEach(new Consumer<U>() {
                            @Override
                            public void accept(U u) {
                                accumulator.accept(a, u);
                            }
                        });
                    }
                },

                downstream.finisher()
        );
    }

    /**
     * Returns a {@code Collector} that performs additional transformation.
     *
     * @param <T> the type of the input elements
     * @param <A> the accumulation type
     * @param <IR> the input type of the transformation function
     * @param <OR> the output type of the transformation function
     * @param c  the input {@code Collector}
     * @param finisher  the final transformation function
     * @return a {@code Collector}
     */
    public static <T, A, IR, OR> Collector<T, A, OR> collectingAndThen(
            Collector<T, A, IR> c, Function<IR, OR> finisher) {
        Function<A, IR> downstreamFinisher = c.finisher();
        if (downstreamFinisher == null) {
            downstreamFinisher = castIdentity();
        }
        return new CollectorsImpl<T, A, OR>(c.supplier(), c.accumulator(),
                Function.Util.andThen(downstreamFinisher, finisher));
    }

    /**
     * Returns a {@code Collector} that performs grouping operation by given classifier.
     *
     * @param <T> the type of the input elements
     * @param <K> the type of the keys
     * @param classifier  the classifier function
     * @return a {@code Collector}
     * @see #groupingBy(com.annimon.stream.function.Function, com.annimon.stream.Collector)
     * @see #groupingBy(com.annimon.stream.function.Function, com.annimon.stream.function.Supplier, com.annimon.stream.Collector)
     */
    public static <T, K> Collector<T, ?, Map<K, List<T>>> groupingBy(
            Function<? super T, ? extends K> classifier) {
        return groupingBy(classifier, Collectors.<T>toList());
    }

    /**
     * Returns a {@code Collector} that performs grouping operation by given classifier.
     *
     * @param <T> the type of the input elements
     * @param <K> the type of the keys
     * @param <A> the accumulation type
     * @param <D> the result type of downstream reduction
     * @param classifier  the classifier function
     * @param downstream  the collector of mapped elements
     * @return a {@code Collector}
     * @see #groupingBy(com.annimon.stream.function.Function)
     * @see #groupingBy(com.annimon.stream.function.Function, com.annimon.stream.function.Supplier, com.annimon.stream.Collector)
     */
    public static <T, K, A, D> Collector<T, ?, Map<K, D>> groupingBy(
            Function<? super T, ? extends K> classifier,
            Collector<? super T, A, D> downstream) {
        return Collectors.<T, K, D, A, Map<K, D>>groupingBy(classifier,
                Collectors.<K, D>hashMapSupplier(), downstream);
    }

    /**
     * Returns a {@code Collector} that performs grouping operation by given classifier.
     *
     * @param <T> the type of the input elements
     * @param <K> the type of the keys
     * @param <A> the accumulation type
     * @param <D> the result type of downstream reduction
     * @param <M> the type of the resulting {@code Map}
     * @param classifier  the classifier function
     * @param mapFactory  a supplier function that provides new {@code Map}
     * @param downstream  the collector of mapped elements
     * @return a {@code Collector}
     * @see #groupingBy(com.annimon.stream.function.Function)
     * @see #groupingBy(com.annimon.stream.function.Function, com.annimon.stream.Collector)
     */
    public static <T, K, D, A, M extends Map<K, D>> Collector<T, ?, M> groupingBy(
            final Function<? super T, ? extends K> classifier,
            final Supplier<M> mapFactory,
            final Collector<? super T, A, D> downstream) {

        @SuppressWarnings("unchecked")
        final Function<A, A> downstreamFinisher = (Function<A, A>) downstream.finisher();
        Function<Map<K, A>, M> finisher = null;
        if (downstreamFinisher != null) {
            finisher = new Function<Map<K, A>, M>() {
                @Override
                public M apply(Map<K, A> map) {
                    // Update values of a map by a finisher function
                    for (Map.Entry<K, A> entry : map.entrySet()) {
                        A value = entry.getValue();
                        value = downstreamFinisher.apply(value);
                        entry.setValue(value);
                    }
                    @SuppressWarnings("unchecked")
                    M castedMap = (M) map;
                    return castedMap;
                }
            };
        }

        @SuppressWarnings("unchecked")
        Supplier<Map<K, A>> castedMapFactory = (Supplier<Map<K, A>>) mapFactory;
        return new CollectorsImpl<T, Map<K, A>, M>(
                castedMapFactory,

                new BiConsumer<Map<K, A>, T>() {
                    @Override
                    public void accept(Map<K, A> map, T t) {
                        K key = Objects.requireNonNull(classifier.apply(t), "element cannot be mapped to a null key");
                        // Get container with currently grouped elements
                        A container = map.get(key);
                        if (container == null) {
                            // Put new container (list, map, set, etc)
                            container = downstream.supplier().get();
                            map.put(key, container);
                        }
                        // Add element to container
                        downstream.accumulator().accept(container, t);
                    }
                },

                finisher
        );
    }

    /**
     * Returns a {@code Collector} that performs partitioning operation according to a predicate.
     * The returned {@code Map} always contains mappings for both {@code false} and {@code true} keys.
     *
     * @param <T> the type of the input elements
     * @param predicate  a predicate used for classifying input elements
     * @return a {@code Collector}
     * @since 1.1.9
     */
    public static <T> Collector<T, ?, Map<Boolean, List<T>>> partitioningBy(
            Predicate<? super T> predicate) {
        return partitioningBy(predicate, Collectors.<T>toList());
    }

    /**
     * Returns a {@code Collector} that performs partitioning operation according to a predicate.
     * The returned {@code Map} always contains mappings for both {@code false} and {@code true} keys.
     *
     * @param <T> the type of the input elements
     * @param <D> the result type of downstream reduction
     * @param <A> the accumulation type
     * @param predicate  a predicate used for classifying input elements
     * @param downstream  the collector of partitioned elements
     * @return a {@code Collector}
     * @since 1.1.9
     */
    public static <T, D, A> Collector<T, ?, Map<Boolean, D>> partitioningBy(
            final Predicate<? super T> predicate,
            final Collector<? super T, A, D> downstream) {

        final BiConsumer<A, ? super T> downstreamAccumulator = downstream.accumulator();
        return new CollectorsImpl<T, Tuple2<A>, Map<Boolean, D>>(
                new Supplier<Tuple2<A>>() {
                    @Override
                    public Tuple2<A> get() {
                        return new Tuple2<A>(
                                downstream.supplier().get(),
                                downstream.supplier().get());
                    }
                },
                new BiConsumer<Tuple2<A>, T>() {
                    @Override
                    public void accept(Tuple2<A> container, T t) {
                        downstreamAccumulator.accept(
                                predicate.test(t) ? container.a : container.b, t);
                    }
                },
                new Function<Tuple2<A>, Map<Boolean, D>>() {
                    @Override
                    public Map<Boolean, D> apply(Tuple2<A> container) {
                        final Function<A, D> downstreamFinisher = downstream.finisher();
                        final Function<A, D> finisher = downstreamFinisher == null
                                ? Collectors.<A, D>castIdentity()
                                : downstreamFinisher;
                        Map<Boolean, D> result = new HashMap<Boolean, D>(2);
                        result.put(Boolean.TRUE, finisher.apply(container.a));
                        result.put(Boolean.FALSE, finisher.apply(container.b));
                        return result;
                    }
                }
        );
    }

    private static <K, V>  Supplier<Map<K, V>> hashMapSupplier() {
        return new Supplier<Map<K, V>>() {

            @Override
            public Map<K, V> get() {
                return new HashMap<K, V>();
            }
        };
    }

    private static IllegalStateException duplicateKeyException(Object key, Object old, Object value) {
        return new IllegalStateException(String.format(
                "Duplicate key %s (attempted merging values %s and %s)",
                key, old, value));
    }

    private static <K, V> void mapMerge(Map<K, V> map, K key, V value, BinaryOperator<V> merger) {
        final V oldValue = map.get(key);
        final V newValue;
        if (oldValue == null) {
            newValue = value;
        } else {
            newValue = merger.apply(oldValue, value);
        }

        if (newValue == null) {
            map.remove(key);
        } else {
            map.put(key, newValue);
        }
    }

    private static <K, V> UnaryOperator<Map<K, V>> toUnmodifiableMapConverter() {
        return new UnaryOperator<Map<K, V>>() {
            @Override
            public Map<K, V> apply(Map<K, V> map) {
                Objects.requireNonNullElements(map.keySet());
                Objects.requireNonNullElements(map.values());
                return Collections.unmodifiableMap(map);
            }
        };
    }

    @SuppressWarnings("unchecked")
    static <A, R> Function<A, R> castIdentity() {
        return new Function<A, R>() {

            @Override
            public R apply(A value) {
                return (R) value;
            }
        };
    }

    private static final class Tuple1<A> {
        A a;

        Tuple1(A a) {
            this.a = a;
        }
    }

    private static final class Tuple2<A> {
        final A a;
        final A b;

        Tuple2(A a, A b) {
            this.a = a;
            this.b = b;
        }
    }

    private static final class CollectorsImpl<T, A, R> implements Collector<T, A, R> {

        private final Supplier<A> supplier;
        private final BiConsumer<A, T> accumulator;
        private final Function<A, R> finisher;

        public CollectorsImpl(Supplier<A> supplier, BiConsumer<A, T> accumulator) {
            this(supplier, accumulator, null);
        }

        public CollectorsImpl(Supplier<A> supplier, BiConsumer<A, T> accumulator, Function<A, R> finisher) {
            this.supplier = supplier;
            this.accumulator = accumulator;
            this.finisher = finisher;
        }

        @Override
        public Supplier<A> supplier() {
            return supplier;
        }

        @Override
        public BiConsumer<A, T> accumulator() {
            return accumulator;
        }

        @Override
        public Function<A, R> finisher() {
            return finisher;
        }

    }
}
