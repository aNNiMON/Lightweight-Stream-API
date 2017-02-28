package com.annimon.stream.longstreamtests;

import com.annimon.stream.Functions;
import com.annimon.stream.LongStream;
import com.annimon.stream.function.LongPredicate;
import org.junit.Test;
import static com.annimon.stream.test.hamcrest.LongStreamMatcher.assertElements;
import static com.annimon.stream.test.hamcrest.LongStreamMatcher.assertIsEmpty;
import static org.hamcrest.Matchers.arrayContaining;

public final class FilterNotTest {

    @Test
    public void testFilterNot() {
        final LongPredicate predicate = Functions.remainderLong(111);
        LongStream.of(322, 555, 666, 1984, 1998)
                .filterNot(predicate)
                .custom(assertElements(arrayContaining(
                        322L, 1984L
                )));

        LongStream.of(777, 999)
                .filterNot(predicate)
                .custom(assertIsEmpty());
    }
}
