package com.annimon.stream.intstreamtests;

import static com.annimon.stream.test.hamcrest.IntStreamMatcher.assertElements;
import static org.hamcrest.Matchers.arrayContaining;

import com.annimon.stream.Functions;
import com.annimon.stream.IntStream;
import com.annimon.stream.test.hamcrest.IntStreamMatcher;
import org.junit.Test;

public final class PrependTest {

    @Test
    public void testPrependEmptyStream() {
        IntStream.empty().prepend(IntStream.empty()).custom(IntStreamMatcher.assertIsEmpty());

        IntStream.of(1, 2).prepend(IntStream.empty()).custom(assertElements(arrayContaining(1, 2)));
    }

    @Test
    public void testPrepend() {
        IntStream.empty().prepend(IntStream.of(0)).custom(assertElements(arrayContaining(0)));
        IntStream.of(1, 2)
                .prepend(IntStream.of(0))
                .custom(assertElements(arrayContaining(0, 1, 2)));
    }

    @Test
    public void testPrependAfterFiltering() {
        IntStream.of(1, 2, 3, 4, 5, 6)
                .filter(Functions.remainderInt(2))
                .prepend(IntStream.of(1))
                .custom(assertElements(arrayContaining(1, 2, 4, 6)));
        IntStream.of(1, 2, 3, 4, 5, 6)
                .filter(Functions.remainderInt(2))
                .prepend(IntStream.of(1, 2, 3, 4).filterNot(Functions.remainderInt(2)))
                .custom(assertElements(arrayContaining(1, 3, 2, 4, 6)));
    }

    @Test
    public void testManyPrepends() {
        IntStream.of(1)
                .prepend(IntStream.of(2))
                .prepend(IntStream.of(3))
                .prepend(IntStream.of(4))
                .custom(assertElements(arrayContaining(4, 3, 2, 1)));
    }

    @Test
    public void testManyPrependsComplex() {
        IntStream.of(1, 2, 3, 4, 5, 6)
                .filter(Functions.remainderInt(2))
                .prepend(IntStream.of(2, 3, 4, 5))
                .filter(Functions.remainderInt(2))
                .limit(4)
                .prepend(IntStream.of(6, 7, 8, 9, 10).filter(Functions.remainderInt(2)))
                .custom(assertElements(arrayContaining(6, 8, 10, 2, 4, 2, 4)));
    }
}
