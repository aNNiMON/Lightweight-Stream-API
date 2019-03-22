package com.annimon.stream;

import com.annimon.stream.function.*;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Common implementations of {@code Collector} interface.
 *
 * @see Collector
 */
@SuppressWarnings({"WeakerAccess", "unused", "RedundantTypeArguments"})
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
    @NotNull
    public static <T, R extends Collection<T>> Collector<T, ?, R> toCollection(
            @NotNull Supplier<R> collectionSupplier) {
        return new CollectorsImpl<T, R, R>(

                collectionSupplier,

                new BiConsumer<R, T>() {
                    @Override
                    public void accept(@NotNull R t, T u) {
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
    @NotNull
    public static <T> Collector<T, ?, List<T>> toList() {
        return new CollectorsImpl<T, List<T>, List<T>>(

                new Supplier<List<T>>() {
                    @NotNull
                    @Override
                    public List<T> get() {
                        return new ArrayList<T>();
                    }
                },

                new BiConsumer<List<T>, T>() {
                    @Override
                    public void accept(@NotNull List<T> t, T u) {
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
    @NotNull
    public static <T> Collector<T, ?, List<T>> toUnmodifiableList() {
        return Collectors.collectingAndThen(Collectors.<T>toList(), new UnaryOperator<List<T>>() {

            @NotNull
            @Override
            public List<T> apply(@NotNull List<T> list) {
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
    @NotNull
    public static <T> Collector<T, ?, Set<T>> toSet() {
        return new CollectorsImpl<T, Set<T>, Set<T>>(

                new Supplier<Set<T>>() {
                    @NotNull
                    @Override
                    public Set<T> get() {
                        return new HashSet<T>();
                    }
                },

                new BiConsumer<Set<T>, T>() {
                    @Override
                    public void accept(@NotNull Set<T> set, T t) {
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
    @NotNull
    public static <T> Collector<T, ?, Set<T>> toUnmodifiableSet() {
        return Collectors.collectingAndThen(Collectors.<T>toSet(), new UnaryOperator<Set<T>>() {

            @NotNull
            @Override
            public Set<T> apply(@NotNull Set<T> set) {
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
    @NotNull
    public static <T, K> Collector<T, ?, Map<K, T>> toMap(
            @NotNull final Function<? super T, ? extends K> keyMapper) {
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
    @NotNull
    public static <T, K, V> Collector<T, ?, Map<K, V>> toMap(
            @NotNull final Function<? super T, ? extends K> keyMapper,
            @NotNull final Function<? super T, ? extends V> valueMapper) {
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
    @NotNull
    public static <T, K, V, M extends Map<K, V>> Collector<T, ?, M> toMap(
            @NotNull final Function<? super T, ? extends K> keyMapper,
            @NotNull final Function<? super T, ? extends V> valueMapper,
            @NotNull final Supplier<M> mapFactory) {
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
    @NotNull
    public static <T, K, V> Collector<T, ?, Map<K, V>> toUnmodifiableMap(
            @NotNull final Function<? super T, ? extends K> keyMapper,
            @NotNull final Function<? super T, ? extends V> valueMapper) {
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
    @NotNull
    public static <T, K, V> Collector<T, ?, Map<K, V>> toMap(
            @NotNull final Function<? super T, ? extends K> keyMapper,
            @NotNull final Function<? super T, ? extends V> valueMapper,
            @NotNull final BinaryOperator<V> mergeFunction) {
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
    @NotNull
    public static <T, K, V, M extends Map<K, V>> Collector<T, ?, M> toMap(
            @NotNull final Function<? super T, ? extends K> keyMapper,
            @NotNull final Function<? super T, ? extends V> valueMapper,
            @NotNull final BinaryOperator<V> mergeFunction,
            @NotNull final Supplier<M> mapFactory) {
        return new CollectorsImpl<T, M, M>(

                mapFactory,

                new BiConsumer<M, T>() {
                    @Override
                    public void accept(@NotNull M map, T t) {
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
    @NotNull
    public static <T, K, V> Collector<T, ?, Map<K, V>> toUnmodifiableMap(
            @NotNull final Function<? super T, ? extends K> keyMapper,
            @NotNull final Function<? super T, ? extends V> valueMapper,
            @NotNull final BinaryOperator<V> mergeFunction) {
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
    @NotNull
    public static Collector<CharSequence, ?, String> joining() {
        return joining("");
    }

    /**
     * Returns a {@code Collector} that concatenates input elements into new string.
     *
     * @param delimiter  the delimiter between each element
     * @return a {@code Collector}
     */
    @NotNull
    public static Collector<CharSequence, ?, String> joining(@NotNull CharSequence delimiter) {
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
    @NotNull
    public static Collector<CharSequence, ?, String> joining(
            @NotNull CharSequence delimiter,
            @NotNull CharSequence prefix,
            @NotNull CharSequence suffix) {
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
    @NotNull
    public static Collector<CharSequence, ?, String> joining(
            @NotNull final CharSequence delimiter,
            @NotNull final CharSequence prefix,
            @NotNull final CharSequence suffix,
            @NotNull final String emptyValue) {
        return new CollectorsImpl<CharSequence, StringBuilder, String>(

                new Supplier<StringBuilder>() {
                    @NotNull
                    @Override
                    public StringBuilder get() {
                        return new StringBuilder();
                    }
                },

                new BiConsumer<StringBuilder, CharSequence>() {
                    @Override
                    public void accept(@NotNull StringBuilder t, CharSequence u) {
                        if (t.length() > 0) {
                            t.append(delimiter);
                        } else {
                            t.append(prefix);
                        }
                        t.append(u);
                    }
                },

                new Function<StringBuilder, String>() {
                    @NotNull
                    @Override
                    public String apply(@NotNull StringBuilder value) {
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
    @NotNull
    public static <T> Collector<T, ?, Double> averaging(@NotNull final Function<? super T, Double> mapper) {
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
    @NotNull
    public static <T> Collector<T, ?, Double> averagingInt(@NotNull final ToIntFunction<? super T> mapper) {
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
    @NotNull
    public static <T> Collector<T, ?, Double> averagingLong(@NotNull final ToLongFunction<? super T> mapper) {
        return averagingHelper(new BiConsumer<long[], T>() {
            @Override
            public void accept(long[] t, T u) {
                t[0]++; // count
                t[1] += mapper.applyAsLong(u); // sum
            }
        });
    }

    @NotNull
    private static <T> Collector<T, ?, Double> averagingHelper(@NotNull final BiConsumer<long[], T> accumulator) {
        return new CollectorsImpl<T, long[], Double>(

                LONG_2ELEMENTS_ARRAY_SUPPLIER,

                accumulator,

                new Function<long[], Double>() {
                    @NotNull
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
    @NotNull
    public static <T> Collector<T, ?, Double> averagingDouble(@NotNull final ToDoubleFunction<? super T> mapper) {
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
                    @NotNull
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
    @NotNull
    public static <T> Collector<T, ?, Integer> summingInt(@NotNull final ToIntFunction<? super T> mapper) {
        return new CollectorsImpl<T, int[], Integer>(

                new Supplier<int[]>() {
                    @NotNull
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
    @NotNull
    public static <T> Collector<T, ?, Long> summingLong(@NotNull final ToLongFunction<? super T> mapper) {
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
    @NotNull
    public static <T> Collector<T, ?, Double> summingDouble(@NotNull final ToDoubleFunction<? super T> mapper) {
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
    @NotNull
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
    @NotNull
    public static <T> Collector<T, ?, T> reducing(@Nullable  final T identity,
                                                  @NotNull final BinaryOperator<T> op) {
        return new CollectorsImpl<T, Tuple1<T>, T>(

                new Supplier<Tuple1<T>>() {
                    @NotNull
                    @Override
                    public Tuple1<T> get() {
                        return new Tuple1<T>(identity);
                    }
                },

                new BiConsumer<Tuple1<T>, T>() {
                    @Override
                    public void accept(@NotNull Tuple1<T> tuple, T value) {
                        tuple.a = op.apply(tuple.a, value);
                    }
                },

                new Function<Tuple1<T>, T>() {
                    @Override
                    public T apply(@NotNull Tuple1<T> tuple) {
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
    @NotNull
    public static <T, R> Collector<T, ?, R> reducing(
            @Nullable  final R identity,
            @NotNull final Function<? super T, ? extends R> mapper,
            @NotNull final BinaryOperator<R> op) {
        return new CollectorsImpl<T, Tuple1<R>, R>(

                new Supplier<Tuple1<R>>() {
                    @NotNull
                    @Override
                    public Tuple1<R> get() {
                        return new Tuple1<R>(identity);
                    }
                },

                new BiConsumer<Tuple1<R>, T>() {
                    @Override
                    public void accept(@NotNull Tuple1<R> tuple, T value) {
                        tuple.a = op.apply(tuple.a, mapper.apply(value));
                    }
                },

                new Function<Tuple1<R>, R>() {
                    @Override
                    public R apply(@NotNull Tuple1<R> tuple) {
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
    @NotNull
    public static <T, A, R> Collector<T, ?, R> filtering(
            @NotNull final Predicate<? super T> predicate,
            @NotNull final Collector<? super T, A, R> downstream) {
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
    @NotNull
    public static <T, U, A, R> Collector<T, ?, R> mapping(
            @NotNull final Function<? super T, ? extends U> mapper,
            @NotNull final Collector<? super U, A, R> downstream) {

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
    @NotNull
    public static <T, U, A, R> Collector<T, ?, R> flatMapping(
            @NotNull final Function<? super T, ? extends Stream<? extends U>> mapper,
            @NotNull final Collector<? super U, A, R> downstream) {

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
    @NotNull
    public static <T, A, IR, OR> Collector<T, A, OR> collectingAndThen(
            @NotNull Collector<T, A, IR> c,
            @NotNull Function<IR, OR> finisher) {
        Objects.requireNonNull(c);
        Objects.requireNonNull(finisher);
        return new CollectorsImpl<T, A, OR>(c.supplier(), c.accumulator(),
                Function.Util.andThen(c.finisher(), finisher));
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
    @NotNull
    public static <T, K> Collector<T, ?, Map<K, List<T>>> groupingBy(
            @NotNull Function<? super T, ? extends K> classifier) {
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
    @NotNull
    public static <T, K, A, D> Collector<T, ?, Map<K, D>> groupingBy(
            @NotNull Function<? super T, ? extends K> classifier,
            @NotNull Collector<? super T, A, D> downstream) {
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
    @NotNull
    public static <T, K, D, A, M extends Map<K, D>> Collector<T, ?, M> groupingBy(
            @NotNull final Function<? super T, ? extends K> classifier,
            @NotNull final Supplier<M> mapFactory,
            @NotNull final Collector<? super T, A, D> downstream) {

        @SuppressWarnings("unchecked")
        final Function<A, A> downstreamFinisher = (Function<A, A>) downstream.finisher();
        Function<Map<K, A>, M> finisher = new Function<Map<K, A>, M>() {
            @NotNull
            @Override
            public M apply(@NotNull Map<K, A> map) {
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

        @SuppressWarnings("unchecked")
        Supplier<Map<K, A>> castedMapFactory = (Supplier<Map<K, A>>) mapFactory;
        return new CollectorsImpl<T, Map<K, A>, M>(
                castedMapFactory,

                new BiConsumer<Map<K, A>, T>() {
                    @Override
                    public void accept(@NotNull Map<K, A> map, T t) {
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
    @NotNull
    public static <T> Collector<T, ?, Map<Boolean, List<T>>> partitioningBy(
            @NotNull Predicate<? super T> predicate) {
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
    @NotNull
    public static <T, D, A> Collector<T, ?, Map<Boolean, D>> partitioningBy(
            @NotNull final Predicate<? super T> predicate,
            @NotNull final Collector<? super T, A, D> downstream) {

        final BiConsumer<A, ? super T> downstreamAccumulator = downstream.accumulator();
        return new CollectorsImpl<T, Tuple2<A>, Map<Boolean, D>>(
                new Supplier<Tuple2<A>>() {
                    @NotNull
                    @Override
                    public Tuple2<A> get() {
                        return new Tuple2<A>(
                                downstream.supplier().get(),
                                downstream.supplier().get());
                    }
                },
                new BiConsumer<Tuple2<A>, T>() {
                    @Override
                    public void accept(@NotNull Tuple2<A> container, T t) {
                        downstreamAccumulator.accept(
                                predicate.test(t) ? container.a : container.b, t);
                    }
                },
                new Function<Tuple2<A>, Map<Boolean, D>>() {
                    @NotNull
                    @Override
                    public Map<Boolean, D> apply(@NotNull Tuple2<A> container) {
                        final Function<A, D> finisher = downstream.finisher();
                        Map<Boolean, D> result = new HashMap<Boolean, D>(2);
                        result.put(Boolean.TRUE, finisher.apply(container.a));
                        result.put(Boolean.FALSE, finisher.apply(container.b));
                        return result;
                    }
                }
        );
    }


    /**
     * Returns a {@code Collector} that composites two collectors.
     * Each element is processed by two specified collectors,
     * then their results are merged using the merge function into the final result.
     *
     * @param <T> the type of the input elements
     * @param <R1> the result type of the first collector
     * @param <R2> the result type of the second collector
     * @param <R> the type of the final result
     * @param downstream1  the first collector
     * @param downstream2  the second collector
     * @param merger  the function which merges two results into the single one
     * @return a {@code Collector}
     * @since 1.2.2
     */
    @NotNull
    public static <T, R1, R2, R> Collector<T, ?, R> teeing(
            @NotNull final Collector<? super T, ?, R1> downstream1,
            @NotNull final Collector<? super T, ?, R2> downstream2,
            @NotNull final BiFunction<? super R1, ? super R2, R> merger) {
        return teeingImpl(downstream1, downstream2, merger);
    }

    private static <T, A1, A2, R1, R2, R> Collector<T, ?, R> teeingImpl(
            @NotNull final Collector<? super T, A1, R1> downstream1,
            @NotNull final Collector<? super T, A2, R2> downstream2,
            @NotNull final BiFunction<? super R1, ? super R2, R> merger) {

        Objects.requireNonNull(downstream1, "downstream1");
        Objects.requireNonNull(downstream2, "downstream2");
        Objects.requireNonNull(merger, "merger");

        final Supplier<A1> supplier1 =
                Objects.requireNonNull(downstream1.supplier(), "downstream1 supplier");
        final Supplier<A2> supplier2 =
                Objects.requireNonNull(downstream2.supplier(), "downstream2 supplier");

        final BiConsumer<A1, ? super T> acc1 =
                Objects.requireNonNull(downstream1.accumulator(), "downstream1 accumulator");
        final BiConsumer<A2, ? super T> acc2 =
                Objects.requireNonNull(downstream2.accumulator(), "downstream2 accumulator");

        final Function<A1, R1> finisher1 =
                Objects.requireNonNull(downstream1.finisher(), "downstream1 finisher");
        final Function<A2, R2> finisher2 =
                Objects.requireNonNull(downstream2.finisher(), "downstream2 finisher");

        return new CollectorsImpl<T, Map.Entry<A1, A2>, R>(
                new Supplier<Map.Entry<A1, A2>>() {
                    @NotNull
                    @Override
                    public Map.Entry<A1, A2> get() {
                        return new AbstractMap.SimpleEntry<A1, A2>(
                                supplier1.get(),
                                supplier2.get());
                    }
                },
                new BiConsumer<Map.Entry<A1, A2>, T>() {
                    @Override
                    public void accept(@NotNull Map.Entry<A1, A2> entry, T t) {
                        acc1.accept(entry.getKey(), t);
                        acc2.accept(entry.getValue(), t);
                    }
                },
                new Function<Map.Entry<A1, A2>, R>() {
                    @NotNull
                    @Override
                    public R apply(@NotNull Map.Entry<A1, A2> entry) {
                        return merger.apply(
                                finisher1.apply(entry.getKey()),
                                finisher2.apply(entry.getValue()));
                    }
                }
        );
    }

    @NotNull
    private static <K, V>  Supplier<Map<K, V>> hashMapSupplier() {
        return new Supplier<Map<K, V>>() {

            @NotNull
            @Override
            public Map<K, V> get() {
                return new HashMap<K, V>();
            }
        };
    }

    @NotNull
    private static IllegalStateException duplicateKeyException(Object key, Object old, Object value) {
        return new IllegalStateException(String.format(
                "Duplicate key %s (attempted merging values %s and %s)",
                key, old, value));
    }

    private static <K, V> void mapMerge(@NotNull Map<K, V> map, K key, V value, @NotNull BinaryOperator<V> merger) {
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

    @NotNull
    private static <K, V> UnaryOperator<Map<K, V>> toUnmodifiableMapConverter() {
        return new UnaryOperator<Map<K, V>>() {
            @NotNull
            @Override
            public Map<K, V> apply(@NotNull Map<K, V> map) {
                Objects.requireNonNullElements(map.keySet());
                Objects.requireNonNullElements(map.values());
                return Collections.unmodifiableMap(map);
            }
        };
    }

    @NotNull
    @SuppressWarnings("unchecked")
    static <A, R> Function<A, R> castIdentity() {
        return new Function<A, R>() {

            @NotNull
            @Override
            public R apply(@NotNull A value) {
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
            this(supplier, accumulator, Collectors.<A, R>castIdentity());
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

        @Nullable
        @Override
        public Function<A, R> finisher() {
            return finisher;
        }

    }
}
