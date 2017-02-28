package com.annimon.stream.intstreamtests;

import com.annimon.stream.IntStream;
import org.junit.Test;
import static com.annimon.stream.test.hamcrest.IntStreamMatcher.assertElements;
import static org.hamcrest.Matchers.arrayContaining;
import static org.junit.Assert.assertTrue;

public final class SkipTest {

    @Test
    public void testSkip() {
        assertTrue(IntStream.empty().skip(2).count() == 0);
        assertTrue(IntStream.range(10, 20).skip(5).count() == 5);
        assertTrue(IntStream.range(10, 20).skip(0).count() == 10);
        assertTrue(IntStream.range(10, 20).skip(10).count() == 0);
        assertTrue(IntStream.range(10, 20).skip(20).count() == 0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSkipNegative() {
        IntStream.empty().skip(-5);
    }

    @Test
    public void testSkipZero() {
        IntStream.of(1,2)
                .skip(0)
                .custom(assertElements(arrayContaining(
                        1, 2
                )));
    }

    @Test
    public void testSkipMoreThanCount() {
        IntStream.range(0, 10)
                .skip(2)
                .limit(5)
                .custom(assertElements(arrayContaining(
                        2, 3, 4, 5, 6
                )));
    }
}
