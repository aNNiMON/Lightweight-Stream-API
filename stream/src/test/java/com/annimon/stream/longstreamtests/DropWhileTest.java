package com.annimon.stream.longstreamtests;

import com.annimon.stream.Functions;
import com.annimon.stream.LongStream;
import org.junit.Test;
import static com.annimon.stream.test.hamcrest.LongStreamMatcher.elements;
import static com.annimon.stream.test.hamcrest.LongStreamMatcher.isEmpty;
import static org.hamcrest.Matchers.arrayContaining;
import static org.junit.Assert.assertThat;

public final class DropWhileTest {

    @Test
    public void testDropWhile() {
        LongStream stream;
        stream = LongStream.of(12, 32, 22, 9, 30, 41, 42)
                    .dropWhile(Functions.remainderLong(2));
        assertThat(stream, elements(arrayContaining(9L, 30L, 41L, 42L)));
    }
    @Test
    public void testDropWhileNonFirstMatch() {
        LongStream stream;
        stream = LongStream.of(5, 32, 22, 9)
                    .dropWhile(Functions.remainderLong(2));
        assertThat(stream, elements(arrayContaining(5L, 32L, 22L, 9L)));
    }

    @Test
    public void testDropWhileAllMatch() {
        LongStream stream;
        stream = LongStream.of(10, 20, 30)
                    .dropWhile(Functions.remainderLong(2));
        assertThat(stream, isEmpty());
    }
}
