package com.annimon.stream.longstreamtests;

import com.annimon.stream.Functions;
import com.annimon.stream.LongStream;
import org.junit.Test;
import static com.annimon.stream.test.hamcrest.LongStreamMatcher.elements;
import static com.annimon.stream.test.hamcrest.LongStreamMatcher.isEmpty;
import static org.hamcrest.Matchers.arrayContaining;
import static org.junit.Assert.assertThat;

public final class TakeWhileTest {

    @Test
    public void testTakeWhile() {
        LongStream stream;
        stream = LongStream.of(12, 32, 22, 9, 30, 41, 42)
                    .takeWhile(Functions.remainderLong(2));
        assertThat(stream, elements(arrayContaining(12L, 32L, 22L)));
    }

    @Test
    public void testTakeWhileNonFirstMatch() {
        LongStream stream;
        stream = LongStream.of(5, 32, 22, 9, 30, 41, 42)
                    .takeWhile(Functions.remainderLong(2));
        assertThat(stream, isEmpty());
    }

    @Test
    public void testTakeWhileAllMatch() {
        LongStream stream;
        stream = LongStream.of(10, 20, 30)
                    .takeWhile(Functions.remainderLong(2));
        assertThat(stream, elements(arrayContaining(10L, 20L, 30L)));
    }
}
