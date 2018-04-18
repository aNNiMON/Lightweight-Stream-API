package com.annimon.stream;

import com.annimon.stream.function.IndexedIntPredicate;
import com.annimon.stream.function.IndexedPredicate;
import com.annimon.stream.function.IntUnaryOperator;
import com.annimon.stream.function.UnaryOperator;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Param;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.infra.Blackhole;

@State(Scope.Benchmark)
public class IndexedBenchmarks {

    private int[] input;

    @Param({"10000000"})
    public int size;

    @Setup
    public void setup() {
        input = new int[size];
        for (int i = 0; i < size; i++) {
            input[i] = i % 10;
        }
    }

    @Benchmark
    public void boxedFilterIndexed(Blackhole bh) {
        bh.consume(IntStream.of(input)
                .boxed()
                .filterIndexed(new IndexedPredicate<Integer>() {
                    @Override
                    public boolean test(int index, Integer value) {
                        return index != value;
                    }
                })
                .count());
    }

    @Benchmark
    public void primitiveFilterIndexed(Blackhole bh) {
        bh.consume(IntStream.of(input)
                .filterIndexed(new IndexedIntPredicate() {
                    @Override
                    public boolean test(int index, int value) {
                        return index != value;
                    }
                })
                .count());
    }


    @Benchmark
    public void boxedFilterIndexedAndMap(Blackhole bh) {
        bh.consume(IntStream.of(input)
                .boxed()
                .filterIndexed(new IndexedPredicate<Integer>() {
                    @Override
                    public boolean test(int index, Integer value) {
                        return index % 5 == 0;
                    }
                })
                .map(new UnaryOperator<Integer>() {
                    @Override
                    public Integer apply(Integer value) {
                        return value * 7;
                    }
                })
                .filterIndexed(new IndexedPredicate<Integer>() {
                    @Override
                    public boolean test(int index, Integer value) {
                        return index != value;
                    }
                })
                .count());
    }

    @Benchmark
    public void primitiveFilterIndexedAndMap(Blackhole bh) {
        bh.consume(IntStream.of(input)
                .filterIndexed(new IndexedIntPredicate() {
                    @Override
                    public boolean test(int index, int value) {
                        return index % 5 == 0;
                    }
                })
                .map(new IntUnaryOperator() {
                    @Override
                    public int applyAsInt(int value) {
                        return value * 7;
                    }
                })
                .filterIndexed(new IndexedIntPredicate() {
                    @Override
                    public boolean test(int index, int value) {
                        return index != value;
                    }
                })
                .count());
    }
}
