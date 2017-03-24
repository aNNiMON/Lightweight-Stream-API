package com.annimon.stream.longstreamtests;

import com.annimon.stream.Functions;
import com.annimon.stream.LongStream;
import com.annimon.stream.function.LongPredicate;
import java.util.NoSuchElementException;
import org.junit.Test;
import static com.annimon.stream.test.hamcrest.LongStreamMatcher.assertElements;
import static com.annimon.stream.test.hamcrest.LongStreamMatcher.assertIsEmpty;
import static org.hamcrest.Matchers.arrayContaining;

public final class FilterTest {

    @Test
    public void testFilter() {
        final LongPredicate predicate = Functions.remainderLong(111);
        LongStream.of(322, 555, 666, 1984, 1998)
                .filter(predicate)
                .custom(assertElements(arrayContaining(
                        555L, 666L, 1998L
                )));

        LongStream.of(12, -10)
                .filter(predicate)
                .custom(assertIsEmpty());
    }

    @Test(expected = NoSuchElementException.class)
    public void testFilterIteratorNextOnEmpty() {
        LongStream.empty()
                .filter(Functions.remainderLong(2))
                .iterator()
                .next();
    }
}
