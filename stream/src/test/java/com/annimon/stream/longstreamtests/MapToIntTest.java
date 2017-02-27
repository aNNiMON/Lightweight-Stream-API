package com.annimon.stream.longstreamtests;

import com.annimon.stream.LongStream;
import com.annimon.stream.function.LongToIntFunction;
import org.junit.Test;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public final class MapToIntTest {

    @Test
    public void testMapToInt() {
        LongToIntFunction mapper = new LongToIntFunction() {
            @Override
            public int applyAsInt(long value) {
                return (int) (value / 10);
            }
        };
        assertThat(LongStream.of(10L, 20L, 30L, 40L).mapToInt(mapper).toArray(),
                is(new int[] {1, 2, 3, 4}));
    }
}
