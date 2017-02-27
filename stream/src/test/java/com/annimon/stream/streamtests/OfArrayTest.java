package com.annimon.stream.streamtests;

import com.annimon.stream.Stream;
import com.annimon.stream.test.hamcrest.StreamMatcher;
import org.junit.Test;

public final class OfArrayTest {

    @Test
    public void testStreamOfEmptyArray() {
        Stream.of(new String[0])
                .custom(StreamMatcher.<String>assertIsEmpty());
    }
}
