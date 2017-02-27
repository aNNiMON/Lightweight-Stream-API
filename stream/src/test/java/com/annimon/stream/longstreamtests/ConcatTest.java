package com.annimon.stream.longstreamtests;

import com.annimon.stream.LongStream;
import org.junit.Test;
import static com.annimon.stream.test.hamcrest.LongStreamMatcher.elements;
import static com.annimon.stream.test.hamcrest.LongStreamMatcher.isEmpty;
import static org.hamcrest.Matchers.arrayContaining;
import static org.junit.Assert.assertThat;

public final class ConcatTest {

    @Test
    public void testStreamConcat() {
        LongStream a1 = LongStream.empty();
        LongStream b1 = LongStream.empty();
        assertThat(LongStream.concat(a1, b1), isEmpty());

        LongStream a2 = LongStream.of(100200300L, 1234567L);
        LongStream b2 = LongStream.empty();
        assertThat(LongStream.concat(a2, b2),
                elements(arrayContaining(100200300L, 1234567L)));

        LongStream a3 = LongStream.of(100200300L, 1234567L);
        LongStream b3 = LongStream.empty();
        assertThat(LongStream.concat(a3, b3),
                elements(arrayContaining(100200300L, 1234567L)));

        LongStream a4 = LongStream.of(-5L, 1234567L, -Integer.MAX_VALUE, Long.MAX_VALUE);
        LongStream b4 = LongStream.of(Integer.MAX_VALUE, 100200300L);
        assertThat(LongStream.concat(a4, b4),
                elements(arrayContaining(-5L, 1234567L, (long) -Integer.MAX_VALUE, Long.MAX_VALUE,
                        (long) Integer.MAX_VALUE, 100200300L)));
    }

    @Test(expected = NullPointerException.class)
    public void testStreamConcatNullA() {
        LongStream.concat(null, LongStream.empty());
    }

    @Test(expected = NullPointerException.class)
    public void testStreamConcatNullB() {
        LongStream.concat(LongStream.empty(), null);
    }
}
