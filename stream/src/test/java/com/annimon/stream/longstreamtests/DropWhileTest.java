package com.annimon.stream.longstreamtests;

import com.annimon.stream.Functions;
import com.annimon.stream.LongStream;
import org.junit.Test;
import static com.annimon.stream.test.hamcrest.LongStreamMatcher.assertElements;
import static com.annimon.stream.test.hamcrest.LongStreamMatcher.assertIsEmpty;
import static org.hamcrest.Matchers.arrayContaining;

public final class DropWhileTest {

    @Test
    public void testDropWhile() {
        LongStream.of(12, 32, 22, 9, 30, 41, 42)
                .dropWhile(Functions.remainderLong(2))
                .custom(assertElements(arrayContaining(
                        9L, 30L, 41L, 42L
                )));
    }
    @Test
    public void testDropWhileNonFirstMatch() {
        LongStream.of(5, 32, 22, 9)
                .dropWhile(Functions.remainderLong(2))
                .custom(assertElements(arrayContaining(
                        5L, 32L, 22L, 9L
                )));
    }

    @Test
    public void testDropWhileAllMatch() {
        LongStream.of(10, 20, 30)
                .dropWhile(Functions.remainderLong(2))
                .custom(assertIsEmpty());
    }
}
