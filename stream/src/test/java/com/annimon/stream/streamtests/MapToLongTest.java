package com.annimon.stream.streamtests;

import com.annimon.stream.LongStream;
import com.annimon.stream.Stream;
import com.annimon.stream.function.ToLongFunction;
import org.junit.Test;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public final class MapToLongTest {

    @Test
    public void testMapToLong() {
        final ToLongFunction<String> stringToSquareLong = new ToLongFunction<String>() {
            @Override
            public long applyAsLong(String t) {
                final String str = t.substring(1, t.length() - 1);
                final long value = Long.parseLong(str);
                return value * value;
            }
        };
        long[] expected = { 4, 9, 16, 64, 625 };
        LongStream stream = Stream.of("[2]", "[3]", "[4]", "[8]", "[25]")
                .mapToLong(stringToSquareLong);
        assertThat(stream.toArray(), is(expected));
    }
}
