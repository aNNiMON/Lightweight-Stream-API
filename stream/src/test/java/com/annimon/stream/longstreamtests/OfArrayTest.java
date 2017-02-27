package com.annimon.stream.longstreamtests;

import com.annimon.stream.LongStream;
import org.junit.Test;
import static com.annimon.stream.test.hamcrest.LongStreamMatcher.assertIsEmpty;
import static com.annimon.stream.test.hamcrest.LongStreamMatcher.elements;
import static org.hamcrest.Matchers.arrayContaining;
import static org.junit.Assert.assertThat;

public final class OfArrayTest {

    @Test
    public void testStreamOfLongs() {
        assertThat(LongStream.of(32, 28), elements(arrayContaining(32L, 28L)));
    }

    @Test(expected = NullPointerException.class)
    public void testStreamOfLongsNull() {
        LongStream.of((long[]) null);
    }

    @Test
    public void testStreamOfEmptyArray() {
        LongStream.of(new long[0])
                .custom(assertIsEmpty());
    }
}
