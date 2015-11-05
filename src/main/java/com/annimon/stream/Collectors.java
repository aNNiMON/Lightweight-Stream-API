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
                    public void accept(Set<T> t, T u) {
                        t.add(u);
                    }
                }
        );
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
        return new CollectorsImpl<T, Map<K, V>, M>(
                
                (Supplier<Map<K, V>>) mapFactory,
                
                new BiConsumer<Map<K, V>, T>() {
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
                }
        );
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
                            return value.toString() + suffix;
                        }
                    }
                }
        );
    }

    public static <T> Collector<T, ?, Double> averaging(final Function<? super T, Double> mapper) {
        return new CollectorsImpl<T, Double[], Double>(
                
                new Supplier<Double[]>() {
                    @Override
                    public Double[] get() {
                        return new Double[] { 0d, 0d };
                    }
                },
                
                new BiConsumer<Double[], T>() {
                    @Override
                    public void accept(Double[] t, T u) {
                        t[0]++; // count
                        t[1] += mapper.apply(u); // sum
                    }
                },
                
                new Function<Double[], Double>() {
                    @Override
                    public Double apply(Double[] t) {
                        return t[1] / t[0];
                    }
                }
        );
    }
    
    public static <T> Collector<T, ?, Long> counting() {
        return new CollectorsImpl<T, Long[], Long>(
                
                new Supplier<Long[]>() {
                    @Override
                    public Long[] get() {
                        return new Long[] { 0L };
                    }
                },
                
                new BiConsumer<Long[], T>() {
                    @Override
                    public void accept(Long[] t, T u) {
                        t[0]++;
                    }
                },
                
                new Function<Long[], Long>() {
                    @Override
                    public Long apply(Long[] value) {
                        return value[0];
                    }
                }
        );
    }
    
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
        
        final Function<A, A> doownstreamFinisher = (Function<A, A>) downstream.finisher();
        Function<Map<K, A>, M> finisher = null;
        if (doownstreamFinisher != null) {
            finisher = new Function<Map<K, A>, M>() {
                @Override
                public M apply(Map<K, A> map) {
                    // Update values of a map by a finisher function
                    for (Map.Entry<K, A> entry : map.entrySet()) {
                        A value = entry.getValue();
                        value = doownstreamFinisher.apply(value);
                        entry.setValue(value);
                    }
                    return (M) map;
                }
            };
        }
        
        return new CollectorsImpl<T, Map<K, A>, M>(
                (Supplier<Map<K, A>>) mapFactory,
                
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
    
    private static <K, V>  Supplier<Map<K, V>> hashMapSupplier() {
        return new Supplier<Map<K, V>>() {

            @Override
            public Map<K, V> get() {
                return new HashMap<K, V>();
            }
        };
    }
    
    private static final class CollectorsImpl<T, A, R> implements Collector<T, A, R> {
        
        private final Supplier<A> suppiler;
        private final BiConsumer<A, T> accumulator;
        private final Function<A, R> finisher;

        public CollectorsImpl(Supplier<A> suppiler, BiConsumer<A, T> accumulator) {
            this(suppiler, accumulator, null);
        }
        
        public CollectorsImpl(Supplier<A> suppiler, BiConsumer<A, T> accumulator, Function<A, R> finisher) {
            this.suppiler = suppiler;
            this.accumulator = accumulator;
            this.finisher = finisher;
        }
        
        @Override
        public Supplier<A> supplier() {
            return suppiler;
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
