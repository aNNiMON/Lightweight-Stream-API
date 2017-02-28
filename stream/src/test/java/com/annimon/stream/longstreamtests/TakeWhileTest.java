package com.annimon.stream.longstreamtests;

import com.annimon.stream.Functions;
import com.annimon.stream.LongStream;
import org.junit.Test;
import static com.annimon.stream.test.hamcrest.LongStreamMatcher.assertElements;
import static com.annimon.stream.test.hamcrest.LongStreamMatcher.assertIsEmpty;
import static org.hamcrest.Matchers.arrayContaining;

public final class TakeWhileTest {

    @Test
    public void testTakeWhile() {
        LongStream.of(12, 32, 22, 9, 30, 41, 42)
                .takeWhile(Functions.remainderLong(2))
                .custom(assertElements(arrayContaining(
                        12L, 32L, 22L
                )));
    }

    @Test
    public void testTakeWhileNonFirstMatch() {
        LongStream.of(5, 32, 22, 9, 30, 41, 42)
                .takeWhile(Functions.remainderLong(2))
                .custom(assertIsEmpty());
    }

    @Test
    public void testTakeWhileAllMatch() {
        LongStream.of(10, 20, 30)
                .takeWhile(Functions.remainderLong(2))
                .custom(assertElements(arrayContaining(
                        10L, 20L, 30L
                )));
    }
}
