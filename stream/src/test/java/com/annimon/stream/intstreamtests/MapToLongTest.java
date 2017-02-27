package com.annimon.stream.intstreamtests;

import com.annimon.stream.IntStream;
import com.annimon.stream.LongStream;
import com.annimon.stream.function.IntToLongFunction;
import com.annimon.stream.test.hamcrest.LongStreamMatcher;
import org.junit.Test;
import static org.hamcrest.Matchers.arrayContaining;
import static org.junit.Assert.assertThat;

public final class MapToLongTest {

    @Test
    public void testMapToLong() {
        LongStream stream = IntStream.rangeClosed(2, 4)
                .mapToLong(new IntToLongFunction() {
                    @Override
                    public long applyAsLong(int value) {
                        return value * 10000000000L;
                    }
                });
        assertThat(stream, LongStreamMatcher.elements(arrayContaining(
                20000000000L,
                30000000000L,
                40000000000L
        )));
    }
}
