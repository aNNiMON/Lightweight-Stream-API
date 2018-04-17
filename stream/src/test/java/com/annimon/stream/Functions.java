package com.annimon.stream;

import com.annimon.stream.function.*;

import java.lang.reflect.Array;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Map;

/**
 * Functions that used in tests.
 */
public final class Functions {

    public static <T> IntFunction<T[]> arrayGenerator(final Class<T[]> clazz) {
        return new IntFunction<T[]>() {

            @Override
            @SuppressWarnings("unchecked")
            public T[] apply(int value) {
                return (T[]) Array.newInstance(clazz.getComponentType(), value);
            }
        };
    }

    public static <T> Function<T, String> convertToString() {
        return new Function<T, String>() {

            @Override
            public String apply(T value) {
                return String.valueOf(value);
            }
        };
    }

    public static IntFunction<String> convertIntToString() {
        return new IntFunction<String>() {

            @Override
            public String apply(int value) {
                return String.valueOf(value);
            }
        };
    }

    public static LongFunction<String> convertLongToString() {
        return new LongFunction<String>() {

            @Override
            public String apply(long value) {
                return String.valueOf(value);
            }
        };
    }

    public static DoubleFunction<String> convertDoubleToString() {
        return new DoubleFunction<String>() {

            @Override
            public String apply(double value) {
                return String.valueOf(value);
            }
        };
    }

    public static Function<String, Integer> stringToInteger() {
        return new Function<String, Integer>() {

            @Override
            public Integer apply(String value) {
                return Integer.parseInt(value);
            }
        };
    }

    public static ToIntFunction<? super Number> toInt() {
        return new ToIntFunction<Number>() {

            @Override
            public int applyAsInt(Number t) {
                return t.intValue();
            }
        };
    }

    public static <K, V> Function<Map.Entry<K, V>, K> entryKey() {
        return new Function<Map.Entry<K, V>, K>() {

            @Override
            public K apply(Map.Entry<K, V> value) {
                return value.getKey();
            }
        };
    }

    public static <K, V> Function<Map.Entry<K, V>, V> entryValue() {
        return new Function<Map.Entry<K, V>, V>() {

            @Override
            public V apply(Map.Entry<K, V> value) {
                return value.getValue();
            }
        };
    }
    public static <T> ToIntFunction<IntPair<T>> intPairIndex() {
        return new ToIntFunction<IntPair<T>>() {

            @Override
            public int applyAsInt(IntPair<T> t) {
                return t.getFirst();
            }
        };
    }

    public static Function<String, Character> firstCharacterExtractor() {
        return new Function<String, Character>() {

            @Override
            public Character apply(String value) {
                return value.charAt(0);
            }
        };
    }

    public static <T> Function<T, Boolean> equalityPartitionItem(final T item) {
        return new Function<T, Boolean>() {

            @Override
            public Boolean apply(T value) {
                return value.equals(item);
            }
        };
    }

    public static Iterator<Integer> counterIterator() {
        return new Iterator<Integer>() {

            private int index = 0;

            @Override
            public boolean hasNext() {
                return true;
            }

            @Override
            public Integer next() {
                return index++;
            }

            @Override
            public void remove() { }
        };
    }

    public static Supplier<Long> fibonacci() {
        return new Supplier<Long>() {

            private long beforePrevious = 0;
            private long previous = 0;

            @Override
            public Long get() {
                final long result = beforePrevious + previous;
                if (result == 0) previous = 1;
                beforePrevious = previous;
                previous = result;
                return result;
            }
        };
    }

    public static Predicate<Integer> remainder(final int val) {
        return new Predicate<Integer>() {

            @Override
            public boolean test(Integer v) {
                return (v % val == 0);
            }
        };
    }

    public static IntPredicate remainderInt(final int val) {
        return new IntPredicate() {

            @Override
            public boolean test(int v) {
                return (v % val == 0);
            }
        };
    }

    public static LongPredicate remainderLong(final long val) {
        return new LongPredicate() {

            @Override
            public boolean test(long v) {
                return (v % val == 0);
            }
        };
    }

    public static DoublePredicate greaterThan(final double value) {
        return new DoublePredicate() {

            @Override
            public boolean test(double v) {
                return v > value;
            }
        };
    }

    public static BiFunction<Integer, Integer, Integer> addition() {
        return new BiFunction<Integer, Integer, Integer>() {

            @Override
            public Integer apply(Integer value1, Integer value2) {
                return value1 + value2;
            }
        };
    }

    public static IndexedBiFunction<Integer, Integer, Integer> indexedAddition() {
        return new IndexedBiFunction<Integer, Integer, Integer>() {
            @Override
            public Integer apply(int index, Integer value1, Integer value2) {
                return index + value1 + value2;
            }
        };
    }

    public static Comparator<Integer> naturalOrder() {
        return new Comparator<Integer>() {

            @Override
            public int compare(Integer o1, Integer o2) {
                return Objects.compareInt(o1, o2);
            }
        };
    }

    public static Comparator<Integer> descendingAbsoluteOrder() {
        return new Comparator<Integer>() {

            @Override
            public int compare(Integer o1, Integer o2) {
                return Objects.compareInt(Math.abs(o2), Math.abs(o1));
            }
        };
    }

    public static Supplier<StringBuilder> stringBuilderSupplier() {
        return new Supplier<StringBuilder>() {

            @Override
            public StringBuilder get() {
                return new StringBuilder();
            }
        };
    }

    public static BiConsumer<StringBuilder, CharSequence> joiningAccumulator() {
        return new BiConsumer<StringBuilder, CharSequence>() {

            @Override
            public void accept(StringBuilder t, CharSequence u) {
                t.append(u);
            }
        };
    }

    public static Collector<CharSequence, ?, String> joiningCollector() {
        return new Collector<CharSequence, StringBuilder, String>() {

            @Override
            public Supplier<StringBuilder> supplier() {
                return stringBuilderSupplier();
            }

            @Override
            public BiConsumer<StringBuilder, CharSequence> accumulator() {
                return joiningAccumulator();
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
}
