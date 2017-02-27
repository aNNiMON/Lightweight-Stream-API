package com.annimon.stream.streamtests;

import com.annimon.stream.LongStream;
import com.annimon.stream.Stream;
import com.annimon.stream.function.Function;
import com.annimon.stream.function.LongUnaryOperator;
import org.junit.Test;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public final class FlatMapToLongTest {

    @Test
    public void testFlatMapToLong() {
        long[] actual = Stream.rangeClosed(2L, 4L)
                .flatMapToLong(new Function<Long, LongStream>() {
                    @Override
                    public LongStream apply(Long t) {
                        return LongStream
                                .iterate(t, LongUnaryOperator.Util.identity())
                                .limit(t);
                    }
                })
                .toArray();

        long[] expected = { 2, 2, 3, 3, 3, 4, 4, 4, 4 };
        assertThat(actual, is(expected));
    }
}
