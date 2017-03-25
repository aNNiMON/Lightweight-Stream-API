package com.annimon.stream;

import com.annimon.stream.function.Function;
import com.annimon.stream.function.ToDoubleFunction;
import com.annimon.stream.function.ToIntFunction;
import com.annimon.stream.function.ToLongFunction;
import java.util.Collections;
import java.util.Comparator;

/**
 * Backported default and static methods from Java 8 {@link java.util.Comparator} interface.
 *
 * @since 1.1.6
 */
public final class ComparatorCompat<T> implements Comparator<T> {

    private static final ComparatorCompat<Comparable<Object>>
            NATURAL_ORDER = new ComparatorCompat<Comparable<Object>>(
                    new Comparator<Comparable<Object>>() {
                        @Override
                        public int compare(Comparable<Object> o1, Comparable<Object> o2) {
                            return o1.compareTo(o2);
                        }
                    });

    private static final ComparatorCompat<Comparable<Object>>
            REVERSE_ORDER = new ComparatorCompat<Comparable<Object>>(
                    Collections.reverseOrder());

    /**
     * Returns a comparator with natural order.
     *
     * @param <T> the type of the objects compared by the comparator
     * @return a comparator
     */
    @SuppressWarnings("unchecked")
    public static <T extends Comparable<? super T>> ComparatorCompat<T> naturalOrder() {
        return (ComparatorCompat<T>) NATURAL_ORDER;
    }

    /**
     * Returns a comparator with reverse order.
     *
     * @param <T> the type of the objects compared by the comparator
     * @return a comparator
     * @see Collections#reverseOrder()
     */
    @SuppressWarnings("unchecked")
    public static <T extends Comparable<? super T>> ComparatorCompat<T> reverseOrder() {
        return (ComparatorCompat<T>) REVERSE_ORDER;
    }

    /**
     * Returns a comparator that reverses the order of the specified comparator.
     * If the specified comparator is {@code null}, this method is equivalent
     * to {@link #reverseOrder()}.
     *
     * @param <T> the type of the objects compared by the comparator
     * @param comparator  a comparator to be reversed
     * @return a comparator
     * @see Collections#reverseOrder(java.util.Comparator)
     * @throws NullPointerException if {@code comparator} is null
     */
    public static <T> Comparator<T> reversed(Comparator<T> comparator) {
        return Collections.reverseOrder(comparator);
    }

    /**
     * Returns a comparator that uses {@code c2} comparator
     * if {@code c1} comparator considers two elements equal.
     *
     * @param <T> the type of the objects compared by the comparators
     * @param c1  a first comparator
     * @param c2  a second comparator
     * @return a comparator
     * @throws NullPointerException if {@code c1} or {@code c2} is null
     */
    public static <T> Comparator<T> thenComparing(
            final Comparator<? super T> c1,
            final Comparator<? super T> c2) {
        Objects.requireNonNull(c1);
        Objects.requireNonNull(c2);
        return new Comparator<T>() {

            @Override
            public int compare(T t1, T t2) {
                final int result = c1.compare(t1, t2);
                return (result != 0) ? result : c2.compare(t1, t2);
            }
        };
    }

    /**
     * Returns a comparator that uses a function that extracts a sort key
     * to be compared with the specified comparator.
     *
     * @param <T> the type of the objects compared by the comparator
     * @param <U> the type of the sort key
     * @param keyExtractor  the function that extracts the sort key
     * @param keyComparator  the comparator used to compare the sort key
     * @return a comparator
     * @throws NullPointerException if {@code keyExtractor} or {@code keyComparator} is null
     */
    public static <T, U> ComparatorCompat<T> comparing(
            final Function<? super T, ? extends U> keyExtractor,
            final Comparator<? super U> keyComparator) {
        Objects.requireNonNull(keyExtractor);
        Objects.requireNonNull(keyComparator);
        return new ComparatorCompat<T>(new Comparator<T>() {

            @Override
            public int compare(T t1, T t2) {
                final U u1 = keyExtractor.apply(t1);
                final U u2 = keyExtractor.apply(t2);
                return keyComparator.compare(u1, u2);
            }
        });
    }

    /**
     * Returns a comparator that uses a function that extracts
     * a {@link java.lang.Comparable} sort key to be compared.
     *
     * @param <T> the type of the objects compared by the comparator
     * @param <U> the type of the sort key
     * @param keyExtractor  the function that extracts the sort key
     * @return a comparator
     * @throws NullPointerException if {@code keyExtractor} is null
     */
    public static <T, U extends Comparable<? super U>> ComparatorCompat<T> comparing(
            final Function<? super T, ? extends U> keyExtractor) {
        Objects.requireNonNull(keyExtractor);
        return new ComparatorCompat<T>(new Comparator<T>() {

            @Override
            public int compare(T t1, T t2) {
                final U u1 = keyExtractor.apply(t1);
                final U u2 = keyExtractor.apply(t2);
                return u1.compareTo(u2);
            }
        });
    }

    /**
     * Returns a comparator that uses a function that extracts
     * an {@code int} sort key to be compared.
     *
     * @param <T> the type of the objects compared by the comparator
     * @param keyExtractor  the function that extracts the sort key
     * @return a comparator
     * @throws NullPointerException if {@code keyExtractor} is null
     */
    public static <T> ComparatorCompat<T> comparingInt(
            final ToIntFunction<? super T> keyExtractor) {
        Objects.requireNonNull(keyExtractor);
        return new ComparatorCompat<T>(new Comparator<T>() {

            @Override
            public int compare(T t1, T t2) {
                final int i1 = keyExtractor.applyAsInt(t1);
                final int i2 = keyExtractor.applyAsInt(t2);
                return Objects.compareInt(i1, i2);
            }
        });
    }

    /**
     * Returns a comparator that uses a function that extracts
     * a {@code long} sort key to be compared.
     *
     * @param <T> the type of the objects compared by the comparator
     * @param keyExtractor  the function that extracts the sort key
     * @return a comparator
     * @throws NullPointerException if {@code keyExtractor} is null
     */
    public static <T> ComparatorCompat<T> comparingLong(
            final ToLongFunction<? super T> keyExtractor) {
        Objects.requireNonNull(keyExtractor);
        return new ComparatorCompat<T>(new Comparator<T>() {

            @Override
            public int compare(T t1, T t2) {
                final long l1 = keyExtractor.applyAsLong(t1);
                final long l2 = keyExtractor.applyAsLong(t2);
                return Objects.compareLong(l1, l2);
            }
        });
    }

    /**
     * Returns a comparator that uses a function that extracts
     * a {@code double} sort key to be compared.
     *
     * @param <T> the type of the objects compared by the comparator
     * @param keyExtractor  the function that extracts the sort key
     * @return a comparator
     * @throws NullPointerException if {@code keyExtractor} is null
     */
    public static <T> ComparatorCompat<T> comparingDouble(
            final ToDoubleFunction<? super T> keyExtractor) {
        Objects.requireNonNull(keyExtractor);
        return new ComparatorCompat<T>(new Comparator<T>() {

            @Override
            public int compare(T t1, T t2) {
                final double d1 = keyExtractor.applyAsDouble(t1);
                final double d2 = keyExtractor.applyAsDouble(t2);
                return Double.compare(d1, d2);
            }
        });
    }

    /**
     * Returns a comparator that considers {@code null} to be
     * less than non-null and all non-null values to be equal.
     *
     * @param <T> the type of the objects compared by the comparator
     * @return a comparator
     */
    public static <T> ComparatorCompat<T> nullsFirst() {
        return nullsComparator(true, null);
    }

    /**
     * Returns a comparator that considers {@code null} to be less than non-null.
     * If the specified comparator is {@code null}, then the returned
     * comparator considers all non-null values to be equal.
     *
     * @param <T> the type of the objects compared by the comparator
     * @param comparator  a comparator for comparing non-null values
     * @return a comparator
     */
    public static <T> ComparatorCompat<T> nullsFirst(Comparator<? super T> comparator) {
        return nullsComparator(true, comparator);
    }

    /**
     * Returns a comparator that considers {@code null} to be
     * greater than non-null and all non-null values to be equal.
     *
     * @param <T> the type of the objects compared by the comparator
     * @return a comparator
     */
    public static <T> ComparatorCompat<T> nullsLast() {
        return nullsComparator(false, null);
    }

    /**
     * Returns a comparator that considers {@code null} to be greater than non-null.
     * If the specified comparator is {@code null}, then the returned
     * comparator considers all non-null values to be equal.
     *
     * @param <T> the type of the objects compared by the comparator
     * @param comparator  a comparator for comparing non-null values
     * @return a comparator
     */
    public static <T> ComparatorCompat<T> nullsLast(Comparator<? super T> comparator) {
        return nullsComparator(false, comparator);
    }

    private static <T> ComparatorCompat<T> nullsComparator(
            final boolean nullFirst, final Comparator<? super T> comparator) {
        return new ComparatorCompat<T>(new Comparator<T>() {

            @Override
            public int compare(T t1, T t2) {
                if (t1 == null) {
                    return (t2 == null) ? 0 : (nullFirst ? -1 : 1);
                } else if (t2 == null) {
                    return nullFirst ? 1 : -1;
                } else {
                    return (comparator == null) ? 0 : comparator.compare(t1, t2);
                }
            }
        });
    }

    /**
     * Allows to build comparators with method chaining.
     *
     * @param <T> the type of the objects compared by the comparator
     * @param comparator  the comparator to be chained
     * @return a {@code ComparatorCompat} instance
     */
    public static <T> ComparatorCompat<T> chain(Comparator<T> comparator) {
        return new ComparatorCompat<T>(comparator);
    }


    private final Comparator<? super T> comparator;

    public ComparatorCompat(Comparator<? super T> comparator) {
        this.comparator = comparator;
    }

    /**
     * Reverses the order of comparator.
     *
     * @return the new {@code ComparatorCompat} instance
     * @see ComparatorCompat#reverseOrder()
     */
    public ComparatorCompat<T> reversed() {
        return new ComparatorCompat<T>(Collections.reverseOrder(comparator));
    }

    /**
     * Adds the given comparator to the chain.
     *
     * @param other  the other comparator to be used when chained
     *               comparator compares two objects that are equal
     * @return the new {@code ComparatorCompat} instance
     */
    public ComparatorCompat<T> thenComparing(final Comparator<? super T> other) {
        Objects.requireNonNull(other);
        return new ComparatorCompat<T>(new Comparator<T>() {

            @Override
            public int compare(T t1, T t2) {
                final int result = comparator.compare(t1, t2);
                return (result != 0) ? result : other.compare(t1, t2);
            }
        });
    }

    /**
     * Adds the comparator, that uses a function for extract
     * a sort key, to the chain.
     *
     * @param <U> the type of the sort key
     * @param keyExtractor  the function that extracts the sort key
     * @param keyComparator  the comparator used to compare the sort key
     * @return the new {@code ComparatorCompat} instance
     */
    public <U> ComparatorCompat<T> thenComparing(
            Function<? super T, ? extends U> keyExtractor,
            Comparator<? super U> keyComparator) {
        return thenComparing(comparing(keyExtractor, keyComparator));
    }

    /**
     * Adds the comparator, that uses a function for extract
     * a {@link java.lang.Comparable} sort key, to the chain.
     *
     * @param <U> the type of the sort key
     * @param keyExtractor  the function that extracts the sort key
     * @return the new {@code ComparatorCompat} instance
     */
    public <U extends Comparable<? super U>> ComparatorCompat<T> thenComparing(
            Function<? super T, ? extends U> keyExtractor) {
        return thenComparing(comparing(keyExtractor));
    }

    /**
     * Adds the comparator, that uses a function for extract
     * an {@code int} sort key, to the chain.
     *
     * @param keyExtractor  the function that extracts the sort key
     * @return the new {@code ComparatorCompat} instance
     */
    public ComparatorCompat<T> thenComparingInt(ToIntFunction<? super T> keyExtractor) {
        return thenComparing(comparingInt(keyExtractor));
    }

    /**
     * Adds the comparator, that uses a function for extract
     * a {@code long} sort key, to the chain.
     *
     * @param keyExtractor  the function that extracts the sort key
     * @return the new {@code ComparatorCompat} instance
     */
    public ComparatorCompat<T> thenComparingLong(ToLongFunction<? super T> keyExtractor) {
        return thenComparing(comparingLong(keyExtractor));
    }

    /**
     * Adds the comparator, that uses a function for extract
     * a {@code double} sort key, to the chain.
     *
     * @param keyExtractor  the function that extracts the sort key
     * @return the new {@code ComparatorCompat} instance
     */
    public ComparatorCompat<T> thenComparingDouble(ToDoubleFunction<? super T> keyExtractor) {
        return thenComparing(comparingDouble(keyExtractor));
    }

    /**
     * Returns a chained {@code Comparator}.
     *
     * @deprecated  As of release 1.1.7, it is unnecessary to call this method.
     * @return a comparator
     */
    @SuppressWarnings("unchecked")
    public Comparator<T> comparator() {
        return (Comparator<T>) comparator;
    }

    @Override
    public int compare(T o1, T o2) {
        return comparator.compare(o1, o2);
    }
}
