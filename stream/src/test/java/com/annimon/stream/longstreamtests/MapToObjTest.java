package com.annimon.stream.longstreamtests;

import com.annimon.stream.LongStream;
import com.annimon.stream.function.LongFunction;
import com.annimon.stream.test.hamcrest.StreamMatcher;
import org.junit.Test;
import static com.annimon.stream.test.hamcrest.StreamMatcher.assertElements;
import static org.hamcrest.Matchers.contains;

public final class MapToObjTest {

    @Test
    public void testMapToObj() {
        LongFunction<String> longToString = new LongFunction<String>() {
            @Override
            public String apply(long value) {
                return Long.toString(value);
            }
        };
        
        LongStream.of(10L, 20L, 30L)
                .mapToObj(longToString)
                .custom(assertElements(contains(
                        "10", "20", "30"
                )));

        LongStream.empty()
                .mapToObj(longToString)
                .custom(StreamMatcher.<String>assertIsEmpty());
    }
}
