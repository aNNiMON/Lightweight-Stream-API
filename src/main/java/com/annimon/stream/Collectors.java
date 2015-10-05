package com.annimon.stream;

import com.annimon.stream.function.BiConsumer;
import com.annimon.stream.function.Function;
import com.annimon.stream.function.Supplier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Common implementations of {@link Collector}.
 * 
 * @author aNNiMON
 */
public final class Collectors {
    
    public static <T> Collector<T, ?, List<T>> toList() {
        return new Collector<T, List<T>, List<T>>() {

            @Override
            public Supplier<List<T>> supplier() {
                return new Supplier<List<T>>() {

                    @Override
                    public List<T> get() {
                        return new ArrayList<T>();
                    }
                };
            }

            @Override
            public BiConsumer<List<T>, T> accumulator() {
                return new BiConsumer<List<T>, T>() {

                    @Override
                    public void accept(List<T> t, T u) {
                        t.add(u);
                    }
                };
            }

            @Override
            public Function<List<T>, List<T>> finisher() {
                return null;
            }
        };
    }
    
    public static <T> Collector<T, ?, Set<T>> toSet() {
        return new Collector<T, Set<T>, Set<T>>() {

            @Override
            public Supplier<Set<T>> supplier() {
                return new Supplier<Set<T>>() {

                    @Override
                    public Set<T> get() {
                        return new HashSet<T>();
                    }
                };
            }

            @Override
            public BiConsumer<Set<T>, T> accumulator() {
                return new BiConsumer<Set<T>, T>() {

                    @Override
                    public void accept(Set<T> t, T u) {
                        t.add(u);
                    }
                };
            }

            @Override
            public Function<Set<T>, Set<T>> finisher() {
                return null;
            }
        };
    }
    
    public static <T, K, V> Collector<T, ?, Map<K, V>> toMap(
            final Function<? super T, ? extends K> keyMapper,
            final Function<? super T, ? extends V> valueMapper) {
        return toMap(keyMapper, valueMapper, Collectors.<K, V>hashMapSupplier());
    }
    
    public static <T, K, V, M extends Map<K, V>> Collector<T, ?, M> toMap(
            final Function<? super T, ? extends K> keyMapper,
            final Function<? super T, ? extends V> valueMapper,
            final Supplier<M> mapFactory) {
        
        return new Collector<T, Map<K, V>, M>() {

            @Override
            public Supplier<Map<K, V>> supplier() {
                return (Supplier<Map<K, V>>) mapFactory;
            }

            @Override
            public BiConsumer<Map<K, V>, T> accumulator() {
                return new BiConsumer<Map<K, V>, T>() {

                    @Override
                    public void accept(Map<K, V> map, T t) {
                        final K key = keyMapper.apply(t);
                        final V value = valueMapper.apply(t);
                        final V oldValue = map.get(key);
                        final V newValue = (oldValue == null) ? value : oldValue;
                        if (newValue == null) {
                            map.remove(key);
                        } else {
                            map.put(key, newValue);
                        }
                    }
                };
            }

            @Override
            public Function<Map<K, V>, M> finisher() {
                return null;
            }
        };
    }
    
    public static Collector<CharSequence, ?, String> joining() {
        return joining("");
    }

    public static Collector<CharSequence, ?, String> joining(CharSequence delimiter) {
        return joining(delimiter, "", "");
    }

    public static Collector<CharSequence, ?, String> joining(CharSequence delimiter, CharSequence prefix, CharSequence suffix) {
        return joining(delimiter, prefix, suffix, prefix.toString() + suffix.toString());
    }

    public static Collector<CharSequence, ?, String> joining(
            final CharSequence delimiter,
            final CharSequence prefix,
            final CharSequence suffix,
            final String emptyValue) {
        return new Collector<CharSequence, StringBuilder, String>() {

            @Override
            public Supplier<StringBuilder> supplier() {
                return new Supplier<StringBuilder>() {

                    @Override
                    public StringBuilder get() {
                        return new StringBuilder();
                    }
                };
            }

            @Override
            public BiConsumer<StringBuilder, CharSequence> accumulator() {
                return new BiConsumer<StringBuilder, CharSequence>() {

                    @Override
                    public void accept(StringBuilder t, CharSequence u) {
                        if (t.length() > 0) {
                            t.append(delimiter);
                        } else {
                            t.append(prefix);
                        }
                        t.append(u);
                    }
                };
            }

            @Override
            public Function<StringBuilder, String> finisher() {
                return new Function<StringBuilder, String>() {

                    @Override
                    public String apply(StringBuilder value) {
                        if (value.length() == 0) {
                            return emptyValue;
                        } else {
                            return value.toString() + suffix;
                        }
                    }
                };
            }
        };
    }

    public static <T> Collector<T, ?, Double> averaging(final Function<? super T, Double> mapper) {
        return new Collector<T, Double[], Double>() {

            @Override
            public Supplier<Double[]> supplier() {
                return new Supplier<Double[]>() {

                    @Override
                    public Double[] get() {
                        return new Double[] { 0d, 0d };
                    }
                };
            }

            @Override
            public BiConsumer<Double[], T> accumulator() {
                return new BiConsumer<Double[], T>() {

                    @Override
                    public void accept(Double[] t, T u) {
                        t[0]++; // count
                        t[1] += mapper.apply(u); // sum
                    }
                };
            }

            @Override
            public Function<Double[], Double> finisher() {
                return new Function<Double[], Double>() {

                    @Override
                    public Double apply(Double[] t) {
                        return t[1] / t[0];
                    }
                };
            }
        };
    }
    
    public static <T> Collector<T, ?, Long> counting() {
        return new Collector<T, Long[], Long>() {

            @Override
            public Supplier<Long[]> supplier() {
                return new Supplier<Long[]>() {

                    @Override
                    public Long[] get() {
                        return new Long[] { 0L };
                    }
                };
            }

            @Override
            public BiConsumer<Long[], T> accumulator() {
                return new BiConsumer<Long[], T>() {

                    @Override
                    public void accept(Long[] t, T u) {
                        t[0]++;
                    }
                };
            }

            @Override
            public Function<Long[], Long> finisher() {
                return new Function<Long[], Long>() {

                    @Override
                    public Long apply(Long[] value) {
                        return value[0];
                    }
                };
            }
        };
    }
    
    public static <T, K> Collector<T, ?, Map<K, List<T>>> groupingBy(
            Function<? super T, ? extends K> classifier) {
        return groupingBy(classifier, Collectors.<T>toList());
    }
    
    public static <T, K, A, D> Collector<T, ?, Map<K, D>> groupingBy(
            Function<? super T, ? extends K> classifier,
            Collector<? super T, A, D> downstream) {
        return groupingBy(classifier, Collectors.<K, D>hashMapSupplier(), downstream);
    }
    
    public static <T, K, D, A, M extends Map<K, D>> Collector<T, ?, M> groupingBy(
            final Function<? super T, ? extends K> classifier,
            final Supplier<M> mapFactory,
            final Collector<? super T, A, D> downstream) {
        return new Collector<T, Map<K, A>, M>() {

            @Override
            public Supplier<Map<K, A>> supplier() {
                return (Supplier<Map<K, A>>) mapFactory;
            }

            @Override
            public BiConsumer<Map<K, A>, T> accumulator() {
                return new BiConsumer<Map<K, A>, T>() {
                    
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
                };
            }

            @Override
            public Function<Map<K, A>, M> finisher() {
                if (downstream.finisher() == null) return null;
                return new Function<Map<K, A>, M>() {

                    @Override
                    public M apply(Map<K, A> map) {
                        final Function<A, A> finisher = (Function<A, A>) downstream.finisher();
                        // Update values of a map by a finisher function
                        for (Map.Entry<K, A> entry : map.entrySet()) {
                            A value = entry.getValue();
                            value = finisher.apply(value);
                            entry.setValue(value);
                        }
                        return (M) map;
                    }
                };
            }
        };
    }
    
    private static <K, V>  Supplier<Map<K, V>> hashMapSupplier() {
        return new Supplier<Map<K, V>>() {

            @Override
            public Map<K, V> get() {
                return new HashMap<K, V>();
            }
        };
    }
}
