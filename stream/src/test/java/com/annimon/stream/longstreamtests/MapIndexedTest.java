package com.annimon.stream.longstreamtests;

import com.annimon.stream.LongStream;
import com.annimon.stream.function.IndexedLongUnaryOperator;
import org.junit.Test;
import static com.annimon.stream.test.hamcrest.LongStreamMatcher.assertElements;
import static org.hamcrest.Matchers.arrayContaining;

public final class MapIndexedTest {

    @Test
    public void testMapIndexed() {
        LongStream.rangeClosed(4, 8)
                .mapIndexed(new IndexedLongUnaryOperator() {
                    @Override
                    public long applyAsLong(int index, long value) {
                        return index * value;
                    }
                })
                .custom(assertElements(arrayContaining(
                       0L,  // (0 * 4)
                       5L,  // (1 * 5)
                       12L, // (2 * 6)
                       21L, // (3 * 7)
                       32L  // (4 * 8)
                )));
    }

    @Test
    public void testMapIndexedWithStartAndStep() {
        LongStream.rangeClosed(4, 8)
                .mapIndexed(20, -5, new IndexedLongUnaryOperator() {
                    @Override
                    public long applyAsLong(int index, long value) {
                        return index * value;
                    }
                })
                .custom(assertElements(arrayContaining(
                       80L, // (20 * 4)
                       75L, // (15 * 5)
                       60L, // (10 * 6)
                       35L, // (5  * 7)
                       0L   // (0  * 8)
                )));
    }
}
