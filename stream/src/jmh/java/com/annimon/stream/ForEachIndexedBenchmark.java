package com.annimon.stream;

import com.annimon.stream.function.IndexedConsumer;
import com.annimon.stream.function.IndexedIntConsumer;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Param;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.infra.Blackhole;

@State(Scope.Benchmark)
public class ForEachIndexedBenchmark {

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
    public void boxedForEachIndexed(final Blackhole bh) {
        IntStream.of(input)
                .boxed()
                .forEachIndexed(new IndexedConsumer<Integer>() {
                    @Override
                    public void accept(int index, Integer value) {
                        bh.consume(index);
                        bh.consume(value);
                    }
                });
    }

    @Benchmark
    public void primitiveForEachIndexed(final Blackhole bh) {
        IntStream.of(input)
                .forEachIndexed(new IndexedIntConsumer() {
                    @Override
                    public void accept(int index, int value) {
                        bh.consume(index);
                        bh.consume(value);
                    }
                });
    }
}
