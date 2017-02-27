package com.annimon.stream.intstreamtests;

import com.annimon.stream.IntStream;
import com.annimon.stream.function.IntFunction;
import java.util.NoSuchElementException;
import org.junit.Test;
import static org.junit.Assert.assertTrue;

public final class FlatMapTest {

    @Test
    public void testFlatMap() {
        assertTrue(IntStream.range(-1, 5).flatMap(new IntFunction<IntStream>() {
            @Override
            public IntStream apply(int value) {

                if(value < 0) {
                    return null;
                }

                if(value == 0) {
                    return IntStream.empty();
                }

                return IntStream.range(0, value);
            }
        }).count() == 10);
    }

    @Test(expected = NoSuchElementException.class)
    public void testFlatMapIterator() {
        IntStream.empty().flatMap(new IntFunction<IntStream>() {
            @Override
            public IntStream apply(int value) {
                return IntStream.of(value);
            }
        }).iterator().nextInt();
    }
}
