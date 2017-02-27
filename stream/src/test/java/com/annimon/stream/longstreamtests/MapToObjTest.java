package com.annimon.stream.longstreamtests;

import com.annimon.stream.LongStream;
import com.annimon.stream.function.LongFunction;
import com.annimon.stream.test.hamcrest.StreamMatcher;
import java.util.Arrays;
import org.junit.Test;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public final class MapToObjTest {

    @Test
    public void testMapToObj() {
        LongFunction<String> longToString = new LongFunction<String>() {
            @Override
            public String apply(long value) {
                return Long.toString(value);
            }
        };
        assertThat(LongStream.of(10L, 20L, 30L).mapToObj(longToString),
                StreamMatcher.elements(is(Arrays.asList("10", "20", "30"))));

        assertThat(LongStream.empty().mapToObj(longToString),
                StreamMatcher.isEmpty());
    }
}
