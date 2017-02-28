package com.annimon.stream.intstreamtests;

import com.annimon.stream.Functions;
import com.annimon.stream.IntStream;
import com.annimon.stream.function.IntPredicate;
import org.junit.Test;
import static com.annimon.stream.test.hamcrest.IntStreamMatcher.assertElements;
import static org.hamcrest.Matchers.arrayContaining;

public final class TakeUntilTest {

    @Test
    public void testTakeUntil() {
        IntStream.of(2, 4, 6, 7, 8, 10, 11)
                .takeUntil(IntPredicate.Util.negate(Functions.remainderInt(2)))
                .custom(assertElements(arrayContaining(
                        2, 4, 6, 7
                )));
    }

    @Test
    public void testTakeUntilFirstMatch() {
        IntStream.of(2, 4, 6, 7, 8, 10, 11)
                .takeUntil(Functions.remainderInt(2))
                .custom(assertElements(arrayContaining(
                        2
                )));
    }

    @Test
    public void testTakeUntilNoneMatch() {
        IntStream.of(2, 4, 6, 7, 8, 10, 11)
                .takeUntil(Functions.remainderInt(128))
                .custom(assertElements(arrayContaining(
                        2, 4, 6, 7, 8, 10, 11
                )));
    }
}
