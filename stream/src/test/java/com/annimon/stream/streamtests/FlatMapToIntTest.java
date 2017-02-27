package com.annimon.stream.streamtests;

import com.annimon.stream.IntStream;
import com.annimon.stream.Stream;
import com.annimon.stream.function.Function;
import com.annimon.stream.function.IntUnaryOperator;
import org.junit.Test;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public final class FlatMapToIntTest {

    @Test
    public void testFlatMapToInt() {
        int[] actual = Stream.rangeClosed(2, 4)
                .flatMapToInt(new Function<Integer, IntStream>() {

                    @Override
                    public IntStream apply(Integer t) {
                        return IntStream
                                .iterate(t, IntUnaryOperator.Util.identity())
                                .limit(t);
                    }
                })
                .toArray();

        int[] expected = { 2, 2, 3, 3, 3, 4, 4, 4, 4 };
        assertThat(actual, is(expected));
    }
}
