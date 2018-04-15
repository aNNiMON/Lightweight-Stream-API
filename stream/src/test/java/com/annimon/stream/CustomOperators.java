package com.annimon.stream;

import com.annimon.stream.function.BinaryOperator;
import com.annimon.stream.function.Consumer;
import com.annimon.stream.function.DoubleBinaryOperator;
import com.annimon.stream.function.Function;
import com.annimon.stream.function.IntBinaryOperator;
import com.annimon.stream.function.LongBinaryOperator;
import com.annimon.stream.iterator.LsaIterator;
import com.annimon.stream.iterator.PrimitiveIterator;
import java.util.ArrayDeque;
import java.util.Iterator;

/**
 * Custom operator examples for {@code Stream.custom} method.
 *
 * @see com.annimon.stream.Stream#custom(com.annimon.stream.function.Function)
 */
public final class CustomOperators {

    private CustomOperators() { }

    /**
     * Example of intermediate operator, that produces reversed stream.
     *
     * @param <T>
     */
    public static class Reverse<T> implements Function<Stream<T>, Stream<T>> {

        @Override
        public Stream<T> apply(Stream<T> stream) {
            final Iterator<? extends T> iterator = stream.iterator();
            final ArrayDeque<T> deque = new ArrayDeque<T>();
            while (iterator.hasNext()) {
                deque.addFirst(iterator.next());
            }
            return Stream.of(deque.iterator());
        }
    }

    /**
     * Example of combining {@code Stream} operators.
     *
     * @param <T>
     */
    public static class SkipAndLimit<T> implements Function<Stream<T>, Stream<T>> {

        private final int skip, limit;

        public SkipAndLimit(int skip, int limit) {
            this.skip = skip;
            this.limit = limit;
        }

        @Override
        public Stream<T> apply(Stream<T> stream) {
            return stream.skip(skip).limit(limit);
        }
    }

    /**
     * Example of terminal operator, that reduces stream to calculate sum on integer elements.
     */
    public static class Sum implements Function<Stream<Integer>, Integer> {

        @Override
        public Integer apply(Stream<Integer> stream) {
            return stream.reduce(0, new BinaryOperator<Integer>() {
                @Override
                public Integer apply(Integer value1, Integer value2) {
                    return value1 + value2;
                }
            });
        }
    }

    /**
     * Example of terminal forEach operator.
     *
     * @param <T>
     */
    public static class ForEach<T> implements Function<Stream<T>, Void> {

        private final Consumer<? super T> action;

        public ForEach(Consumer<? super T> action) {
            this.action = action;
        }

        @Override
        public Void apply(Stream<T> stream) {
            final Iterator<? extends T> iterator = stream.iterator();
            while (iterator.hasNext()) {
                action.accept(iterator.next());
            }
            return null;
        }
    }

    /**
     * Example of intermediate operator, that casts elements in the stream.
     *
     * @param <T>
     * @param <R>
     */
    public static class Cast<T, R> implements Function<Stream<T>, Stream<R>> {

        private final Class<R> clazz;

        public Cast(Class<R> clazz) {
            this.clazz = clazz;
        }

        @Override
        public Stream<R> apply(final Stream<T> stream) {
            final Iterator<? extends T> iterator = stream.iterator();
            return Stream.of(new LsaIterator<R>() {

                @Override
                public boolean hasNext() {
                    return iterator.hasNext();
                }

                @Override
                public R nextIteration() {
                    return clazz.cast(iterator.next());
                }
            });
        }
    }

    /**
     * Example of intermediate flatMap operator.
     *
     * @param <T>
     * @param <R>
     */
    public static class FlatMap<T, R> implements Function<Stream<T>, Stream<R>> {

        private final Function<? super T, ? extends Stream<R>> mapper;

        public FlatMap(Function<? super T, ? extends Stream<R>> mapper) {
            this.mapper = mapper;
        }

        @Override
        public Stream<R> apply(final Stream<T> stream) {
            final Iterator<? extends T> iterator = stream.iterator();
            return Stream.of(new LsaIterator<R>() {

                private Iterator<? extends R> inner;

                @Override
                public boolean hasNext() {
                    fillInnerIterator();
                    return inner != null && inner.hasNext();
                }

                @Override
                public R nextIteration() {
                    fillInnerIterator();
                    return inner.next();
                }

                private void fillInnerIterator() {
                    if ((inner != null) && inner.hasNext()) {
                        return;
                    }
                    while (iterator.hasNext()) {
                        final T arg = iterator.next();
                        final Stream<? extends R> result = mapper.apply(arg);
                        if (result != null) {
                            inner = result.iterator();
                            if (inner.hasNext()) {
                                return;
                            }
                        }
                    }
                }

            });
        }
    }

    /**
     * Example of intermediate zip operator applying on two {@code IntStream}.
     */
    public static class Zip implements Function<IntStream, IntStream> {

        private final IntStream secondStream;
        private final IntBinaryOperator combiner;

        public Zip(IntStream secondStream, IntBinaryOperator combiner) {
            this.secondStream = secondStream;
            this.combiner = combiner;
        }

        @Override
        public IntStream apply(IntStream firstStream) {
            final PrimitiveIterator.OfInt it1 = firstStream.iterator();
            final PrimitiveIterator.OfInt it2 = secondStream.iterator();
            return IntStream.of(new PrimitiveIterator.OfInt() {

                @Override
                public boolean hasNext() {
                    return it1.hasNext() && it2.hasNext();
                }

                @Override
                public int nextInt() {
                    return combiner.applyAsInt(it1.nextInt(), it2.nextInt());
                }
            });
        }
    }

    /**
     * Example of intermediate zip operator applying on two {@code LongStream}.
     */
    public static class ZipLong implements Function<LongStream, LongStream> {

        private final LongStream secondStream;
        private final LongBinaryOperator combiner;

        public ZipLong(LongStream secondStream, LongBinaryOperator combiner) {
            this.secondStream = secondStream;
            this.combiner = combiner;
        }

        @Override
        public LongStream apply(LongStream firstStream) {
            final PrimitiveIterator.OfLong it1 = firstStream.iterator();
            final PrimitiveIterator.OfLong it2 = secondStream.iterator();
            return LongStream.of(new PrimitiveIterator.OfLong() {

                @Override
                public boolean hasNext() {
                    return it1.hasNext() && it2.hasNext();
                }

                @Override
                public long nextLong() {
                    return combiner.applyAsLong(it1.nextLong(), it2.nextLong());
                }
            });
        }
    }

    /**
     * Example of terminal operator that calculates arithmetic mean of values.
     */
    public static class Average implements Function<IntStream, Double> {

        @Override
        public Double apply(IntStream stream) {
            long count = 0, sum = 0;
            final PrimitiveIterator.OfInt it = stream.iterator();
            while (it.hasNext()) {
                count++;
                sum += it.nextInt();
            }
            return (count == 0) ? 0 : sum / (double) count;
        }
    }

    /**
     * Example of terminal operator that calculates arithmetic mean of values.
     */
    public static class AverageLong implements Function<LongStream, Double> {

        @Override
        public Double apply(LongStream stream) {
            long count = 0, sum = 0;
            final PrimitiveIterator.OfLong it = stream.iterator();
            while (it.hasNext()) {
                count++;
                sum += it.nextLong();
            }
            return (count == 0) ? 0 : sum / (double) count;
        }
    }

    /**
     * Example of intermediate zip operator applying on {@code DoubleStream} and {@code IntStream}.
     */
    public static class ZipWithIntStream implements Function<DoubleStream, DoubleStream> {

        private final IntStream secondStream;
        private final DoubleBinaryOperator combiner;

        public ZipWithIntStream(IntStream secondStream, DoubleBinaryOperator combiner) {
            this.secondStream = secondStream;
            this.combiner = combiner;
        }

        @Override
        public DoubleStream apply(DoubleStream firstStream) {
            final PrimitiveIterator.OfDouble it1 = firstStream.iterator();
            final PrimitiveIterator.OfInt it2 = secondStream.iterator();
            return DoubleStream.of(new PrimitiveIterator.OfDouble() {

                @Override
                public boolean hasNext() {
                    return it1.hasNext() && it2.hasNext();
                }

                @Override
                public double nextDouble() {
                    return combiner.applyAsDouble(it1.nextDouble(), it2.nextInt());
                }
            });
        }
    }

    /**
     * Example of terminal operator that calculates count, sum, and arithmetic mean of values.
     */
    public static class DoubleSummaryStatistics implements Function<DoubleStream, double[]> {

        @Override
        public double[] apply(DoubleStream stream) {
            long count = 0;
            double sum = 0;
            final PrimitiveIterator.OfDouble it = stream.iterator();
            while (it.hasNext()) {
                count++;
                sum += it.nextDouble();
            }
            double average = (count == 0) ? 0 : (sum / (double) count);
            return new double[] {count, sum, average};
        }
    }
}
