package com.annimon.stream.intstreamtests;

import com.annimon.stream.Functions;
import com.annimon.stream.IntStream;
import org.junit.Test;
import static com.annimon.stream.test.hamcrest.IntStreamMatcher.assertElements;
import static com.annimon.stream.test.hamcrest.IntStreamMatcher.assertIsEmpty;
import static org.hamcrest.Matchers.arrayContaining;

public final class DropWhileTest {

    @Test
    public void testDropWhile() {
        IntStream.of(2, 4, 6, 7, 8, 10, 11)
                .dropWhile(Functions.remainderInt(2))
                .custom(assertElements(arrayContaining(
                        7, 8, 10, 11
                )));
    }

    @Test
    public void testDropWhileNonFirstMatch() {
        IntStream.of(2, 4, 6, 7, 8, 10, 11)
                .dropWhile(Functions.remainderInt(3))
                .custom(assertElements(arrayContaining(
                        2, 4, 6, 7, 8, 10, 11
                )));
    }

    @Test
    public void testDropWhileAllMatch() {
        IntStream.of(2, 4, 6, 7, 8, 10, 11)
                .dropWhile(Functions.remainderInt(1))
                .custom(assertIsEmpty());
    }
}
