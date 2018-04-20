package com.annimon.stream;

import com.annimon.stream.function.IndexedConsumer;
import com.annimon.stream.function.IndexedFunction;
import com.annimon.stream.function.IndexedIntConsumer;
import com.annimon.stream.function.IndexedIntPredicate;
import com.annimon.stream.function.IndexedPredicate;
import com.annimon.stream.function.IntBinaryOperator;
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
    public void boxedIndexed(final Blackhole bh) {
        IntStream.of(input)
                .boxed()
                .filterIndexed(new IndexedPredicate<Integer>() {
                    @Override
                    public boolean test(int index, Integer value) {
                        return index % 5 == 0;
                    }
                })
                .mapIndexed(new IndexedFunction<Integer, Integer>() {
                    @Override
                    public Integer apply(int index, Integer value) {
                        return (index + value) / 2;
                    }
                })
                .forEachIndexed(new IndexedConsumer<Integer>() {
                    @Override
                    public void accept(int index, Integer value) {
                        bh.consume(index);
                        bh.consume(value);
                    }
                });
    }

    @Benchmark
    public void primitiveIndexed(final Blackhole bh) {
        IntStream.of(input)
                .filterIndexed(new IndexedIntPredicate() {
                    @Override
                    public boolean test(int index, int value) {
                        return index % 5 == 0;
                    }
                })
                .mapIndexed(new IntBinaryOperator() {
                    @Override
                    public int applyAsInt(int index, int value) {
                        return (index + value) / 2;
                    }
                })
                .forEachIndexed(new IndexedIntConsumer() {
                    @Override
                    public void accept(int index, int value) {
                        bh.consume(index);
                        bh.consume(value);
                    }
                });
    }
}
