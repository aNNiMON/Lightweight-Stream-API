package com.annimon.stream.longstreamtests;

import com.annimon.stream.LongStream;
import org.junit.Test;
import static com.annimon.stream.test.hamcrest.LongStreamMatcher.assertElements;
import static com.annimon.stream.test.hamcrest.LongStreamMatcher.assertIsEmpty;
import static org.hamcrest.Matchers.arrayContaining;

public final class ConcatTest {

    @Test
    public void testStreamConcat() {
        LongStream a1 = LongStream.empty();
        LongStream b1 = LongStream.empty();
        LongStream.concat(a1, b1).custom(assertIsEmpty());

        LongStream a2 = LongStream.of(100200300L, 1234567L);
        LongStream b2 = LongStream.empty();
        LongStream.concat(a2, b2)
                .custom(assertElements(arrayContaining(
                        100200300L, 1234567L
                )));

        LongStream a3 = LongStream.of(100200300L, 1234567L);
        LongStream b3 = LongStream.empty();
        LongStream.concat(a3, b3)
                .custom(assertElements(arrayContaining(
                        100200300L, 1234567L
                )));

        LongStream a4 = LongStream.of(-5L, 1234567L, -Integer.MAX_VALUE, Long.MAX_VALUE);
        LongStream b4 = LongStream.of(Integer.MAX_VALUE, 100200300L);
        LongStream.concat(a4, b4)
                .custom(assertElements(arrayContaining(
                        -5L, 1234567L, (long) -Integer.MAX_VALUE, Long.MAX_VALUE,
                        (long) Integer.MAX_VALUE, 100200300L
                )));
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
