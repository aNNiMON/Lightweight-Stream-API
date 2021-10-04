package com.annimon.stream.longstreamtests;

import static com.annimon.stream.test.hamcrest.LongStreamMatcher.assertElements;
import static org.hamcrest.Matchers.arrayContaining;

import com.annimon.stream.Functions;
import com.annimon.stream.LongStream;
import com.annimon.stream.test.hamcrest.LongStreamMatcher;
import org.junit.Test;

public final class AppendTest {

    @Test
    public void testAppendEmptyStream() {
        LongStream.empty().append(LongStream.empty()).custom(LongStreamMatcher.assertIsEmpty());

        LongStream.of(1, 2)
                .append(LongStream.empty())
                .custom(assertElements(arrayContaining(1L, 2L)));
    }

    @Test
    public void testAppend() {
        LongStream.empty().append(LongStream.of(0)).custom(assertElements(arrayContaining(0L)));
        LongStream.of(1, 2)
                .append(LongStream.of(0))
                .custom(assertElements(arrayContaining(1L, 2L, 0L)));
    }

    @Test
    public void testAppendAfterFiltering() {
        LongStream.of(1, 2, 3, 4, 5, 6)
                .filter(Functions.remainderLong(2))
                .append(LongStream.of(1))
                .custom(assertElements(arrayContaining(2L, 4L, 6L, 1L)));
        LongStream.of(1, 2, 3, 4, 5, 6)
                .filter(Functions.remainderLong(2))
                .append(LongStream.of(1, 2, 3, 4).filterNot(Functions.remainderLong(2)))
                .custom(assertElements(arrayContaining(2L, 4L, 6L, 1L, 3L)));
    }

    @Test
    public void testManyAppends() {
        LongStream.of(1)
                .append(LongStream.of(2))
                .append(LongStream.of(3))
                .append(LongStream.of(4))
                .custom(assertElements(arrayContaining(1L, 2L, 3L, 4L)));
    }

    @Test
    public void testManyAppendsComplex() {
        LongStream.of(1, 2, 3, 4, 5, 6)
                .filter(Functions.remainderLong(2))
                .append(LongStream.of(2, 3, 4, 5))
                .filter(Functions.remainderLong(2))
                .limit(4)
                .append(LongStream.of(6, 7, 8, 9, 10).filter(Functions.remainderLong(2)))
                .custom(assertElements(arrayContaining(2L, 4L, 6L, 2L, 6L, 8L, 10L)));
    }
}
