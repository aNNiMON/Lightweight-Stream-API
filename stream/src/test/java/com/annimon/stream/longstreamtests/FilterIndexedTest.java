package com.annimon.stream.longstreamtests;

import com.annimon.stream.Functions;
import com.annimon.stream.LongStream;
import com.annimon.stream.function.IndexedLongPredicate;
import java.util.NoSuchElementException;
import org.junit.Test;
import static com.annimon.stream.test.hamcrest.LongStreamMatcher.assertElements;
import static org.hamcrest.Matchers.arrayContaining;

public final class FilterIndexedTest {

    @Test
    public void testFilterIndexed() {
        LongStream.rangeClosed(4, 8)
                .filterIndexed(new IndexedLongPredicate() {
                    @Override
                    public boolean test(int index, long value) {
                        return (index * value) % 2 == 0;
                    }
                })
                .custom(assertElements(arrayContaining(
                        4L, // (0 * 4)
                            // (1 * 5)
                        6L, // (2 * 6)
                            // (3 * 7)
                        8L  // (4 * 8)
                )));
    }

    @Test
    public void testFilterIndexedWithStartAndStep() {
        LongStream.rangeClosed(4, 8)
                .filterIndexed(20, -5, new IndexedLongPredicate() {
                    @Override
                    public boolean test(int index, long value) {
                        return (index * value) % 2 == 0;
                    }
                })
                .custom(assertElements(arrayContaining(
                        4L, // (20 * 4)
                            // (15 * 5)
                        6L, // (10 * 6)
                            // (5  * 7)
                        8L  // (0  * 8)
                )));
    }

    @Test(expected = NoSuchElementException.class)
    public void testFilterIndexedIteratorNextOnEmpty() {
        LongStream.empty()
                .filterIndexed(IndexedLongPredicate.Util.wrap(Functions.remainderLong(2)))
                .iterator()
                .next();
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testFilterIndexedIteratorRemove() {
        LongStream.range(0, 10)
                .filterIndexed(IndexedLongPredicate.Util.wrap(Functions.remainderLong(2)))
                .iterator()
                .remove();
    }
}
