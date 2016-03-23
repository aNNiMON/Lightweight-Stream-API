package com.annimon.stream;

import com.annimon.stream.function.BinaryOperator;
import com.annimon.stream.function.Consumer;
import com.annimon.stream.function.Function;
import java.util.ArrayDeque;
import java.util.Iterator;

/**
 * Custom operator examples for {@code Stream.custom) method.
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
            final Iterator<? extends T> iterator = stream.getIterator();
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
            final Iterator<? extends T> iterator = stream.getIterator();
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
            final Iterator<? extends T> iterator = stream.getIterator();
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
     */
    public static class FlatMap<T, R> implements Function<Stream<T>, Stream<R>> {

        private final Function<? super T, ? extends Stream<R>> mapper;

        public FlatMap(Function<? super T, ? extends Stream<R>> mapper) {
            this.mapper = mapper;
        }
        
        @Override
        public Stream<R> apply(final Stream<T> stream) {
            final Iterator<? extends T> iterator = stream.getIterator();
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
                            inner = result.getIterator();
                            if (inner.hasNext()) {
                                return;
                            }
                        }
                    }
                }

            });
        }
    }
}
