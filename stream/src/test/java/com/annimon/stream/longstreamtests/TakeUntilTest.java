package com.annimon.stream.longstreamtests;

import com.annimon.stream.Functions;
import com.annimon.stream.LongStream;
import com.annimon.stream.function.LongPredicate;
import org.junit.Test;
import static com.annimon.stream.test.hamcrest.LongStreamMatcher.assertElements;
import static org.hamcrest.Matchers.arrayContaining;

public final class TakeUntilTest {

    @Test
    public void testTakeUntil() {
        LongStream.of(2, 4, 6, 7, 8, 10, 11)
                .takeUntil(LongPredicate.Util.negate(Functions.remainderLong(2)))
                .custom(assertElements(arrayContaining(
                        2L, 4L, 6L, 7L
                )));
    }

    @Test
    public void testTakeUntilFirstMatch() {
        LongStream.of(2, 4, 6, 7, 8, 10, 11)
                .takeUntil(Functions.remainderLong(2))
                .custom(assertElements(arrayContaining(
                        2L
                )));
    }

    @Test
    public void testTakeUntilNoneMatch() {
        LongStream.of(2, 4, 6, 7, 8, 10, 11)
                .takeUntil(Functions.remainderLong(128))
                .custom(assertElements(arrayContaining(
                        2L, 4L, 6L, 7L, 8L, 10L, 11L
                )));
    }
}
