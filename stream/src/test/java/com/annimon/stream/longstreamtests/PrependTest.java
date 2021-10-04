package com.annimon.stream.longstreamtests;

import static com.annimon.stream.test.hamcrest.LongStreamMatcher.assertElements;
import static org.hamcrest.Matchers.arrayContaining;

import com.annimon.stream.Functions;
import com.annimon.stream.LongStream;
import com.annimon.stream.test.hamcrest.LongStreamMatcher;
import org.junit.Test;

public final class PrependTest {

    @Test
    public void testPrependEmptyStream() {
        LongStream.empty().prepend(LongStream.empty()).custom(LongStreamMatcher.assertIsEmpty());

        LongStream.of(1, 2)
                .prepend(LongStream.empty())
                .custom(assertElements(arrayContaining(1L, 2L)));
    }

    @Test
    public void testPrepend() {
        LongStream.empty().prepend(LongStream.of(0)).custom(assertElements(arrayContaining(0L)));
        LongStream.of(1, 2)
                .prepend(LongStream.of(0))
                .custom(assertElements(arrayContaining(0L, 1L, 2L)));
    }

    @Test
    public void testPrependAfterFiltering() {
        LongStream.of(1, 2, 3, 4, 5, 6)
                .filter(Functions.remainderLong(2))
                .prepend(LongStream.of(1))
                .custom(assertElements(arrayContaining(1L, 2L, 4L, 6L)));
        LongStream.of(1, 2, 3, 4, 5, 6)
                .filter(Functions.remainderLong(2))
                .prepend(LongStream.of(1, 2, 3, 4).filterNot(Functions.remainderLong(2)))
                .custom(assertElements(arrayContaining(1L, 3L, 2L, 4L, 6L)));
    }

    @Test
    public void testManyPrepends() {
        LongStream.of(1)
                .prepend(LongStream.of(2))
                .prepend(LongStream.of(3))
                .prepend(LongStream.of(4))
                .custom(assertElements(arrayContaining(4L, 3L, 2L, 1L)));
    }

    @Test
    public void testManyPrependsComplex() {
        LongStream.of(1, 2, 3, 4, 5, 6)
                .filter(Functions.remainderLong(2))
                .prepend(LongStream.of(2, 3, 4, 5))
                .filter(Functions.remainderLong(2))
                .limit(4)
                .prepend(LongStream.of(6, 7, 8, 9, 10).filter(Functions.remainderLong(2)))
                .custom(assertElements(arrayContaining(6L, 8L, 10L, 2L, 4L, 2L, 4L)));
    }
}
