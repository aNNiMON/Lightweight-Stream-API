package com.annimon.stream.longstreamtests;

import com.annimon.stream.LongStream;
import org.junit.Test;
import static com.annimon.stream.test.hamcrest.LongStreamMatcher.assertElements;
import static org.hamcrest.Matchers.arrayContaining;

public final class DistinctTest {

    @Test
    public void testDistinct() {
        LongStream.of(9, 12, 0, 22, 9, 12, 32, 9)
                .distinct()
                .custom(assertElements(arrayContaining(
                        9L, 12L, 0L, 22L, 32L
                )));

        LongStream.of(8, 800, 5, 5, 5, 3, 5, 3, 5)
                .distinct()
                .custom(assertElements(arrayContaining(
                        8L, 800L, 5L, 3L
                )));
    }
}
