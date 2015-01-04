package com.annimon.stream;

import com.annimon.stream.function.BiConsumer;
import com.annimon.stream.function.Function;
import com.annimon.stream.function.Supplier;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/**
 * Common implementations of {@link Collector}.
 * 
 * @author aNNiMON
 */
public final class Collectors {
    
    public static <T> Collector<T, ?, List<T>> toList(T[] typeResolver) {
        return new Collector<T, List<T>, List<T>>() {

            @Override
            public Supplier<List<T>> supplier() {
                return new Supplier<List<T>>() {

                    @Override
                    public List<T> get() {
                        return new LinkedList<T>();
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
    
    public static <T> Collector<T, ?, Set<T>> toSet(T[] typeResolver) {
        return new Collector<T, Set<T>, Set<T>>() {

            @Override
            public Supplier<Set<T>> supplier() {
                return new Supplier<Set<T>>() {

                    @Override
                    public Set<T> get() {
                        return new LinkedHashSet<T>();
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
    
    public static Collector<CharSequence, ?, String> joining() {
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
                        t.append(u);
                    }
                };
            }

            @Override
            public Function<StringBuilder, String> finisher() {
                return new Function<StringBuilder, String>() {

                    @Override
                    public String apply(StringBuilder value) {
                        return value.toString();
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
}
