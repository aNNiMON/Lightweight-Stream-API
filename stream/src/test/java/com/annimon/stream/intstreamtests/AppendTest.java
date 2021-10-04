package com.annimon.stream.intstreamtests;

import static com.annimon.stream.test.hamcrest.IntStreamMatcher.assertElements;
import static org.hamcrest.Matchers.arrayContaining;

import com.annimon.stream.Functions;
import com.annimon.stream.IntStream;
import com.annimon.stream.test.hamcrest.IntStreamMatcher;
import org.junit.Test;

public final class AppendTest {

    @Test
    public void testAppendEmptyStream() {
        IntStream.empty().append(IntStream.empty()).custom(IntStreamMatcher.assertIsEmpty());

        IntStream.of(1, 2).append(IntStream.empty()).custom(assertElements(arrayContaining(1, 2)));
    }

    @Test
    public void testAppend() {
        IntStream.empty().append(IntStream.of(0)).custom(assertElements(arrayContaining(0)));
        IntStream.of(1, 2).append(IntStream.of(0)).custom(assertElements(arrayContaining(1, 2, 0)));
    }

    @Test
    public void testAppendAfterFiltering() {
        IntStream.of(1, 2, 3, 4, 5, 6)
                .filter(Functions.remainderInt(2))
                .append(IntStream.of(1))
                .custom(assertElements(arrayContaining(2, 4, 6, 1)));
        IntStream.of(1, 2, 3, 4, 5, 6)
                .filter(Functions.remainderInt(2))
                .append(IntStream.of(1, 2, 3, 4).filterNot(Functions.remainderInt(2)))
                .custom(assertElements(arrayContaining(2, 4, 6, 1, 3)));
    }

    @Test
    public void testManyAppends() {
        IntStream.of(1)
                .append(IntStream.of(2))
                .append(IntStream.of(3))
                .append(IntStream.of(4))
                .custom(assertElements(arrayContaining(1, 2, 3, 4)));
    }

    @Test
    public void testManyAppendsComplex() {
        IntStream.of(1, 2, 3, 4, 5, 6)
                .filter(Functions.remainderInt(2))
                .append(IntStream.of(2, 3, 4, 5))
                .filter(Functions.remainderInt(2))
                .limit(4)
                .append(IntStream.of(6, 7, 8, 9, 10).filter(Functions.remainderInt(2)))
                .custom(assertElements(arrayContaining(2, 4, 6, 2, 6, 8, 10)));
    }
}
