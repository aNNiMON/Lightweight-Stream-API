package com.annimon.stream.doublestreamtests;

import com.annimon.stream.Functions;
import com.annimon.stream.DoubleStream;
import com.annimon.stream.function.IndexedDoublePredicate;
import java.util.NoSuchElementException;
import org.junit.Test;
import static com.annimon.stream.test.hamcrest.DoubleStreamMatcher.assertElements;
import static org.hamcrest.Matchers.arrayContaining;
import static org.hamcrest.Matchers.closeTo;

public final class FilterIndexedTest {

    @Test
    @SuppressWarnings("unchecked")
    public void testFilterIndexed() {
        DoubleStream.of(1, 12, 3, 8, 2)
                .filterIndexed(new IndexedDoublePredicate() {
                    @Override
                    public boolean test(int index, double value) {
                        return (index * value) > 10;
                    }
                })
                .custom(assertElements(arrayContaining(
                        // (0 * 1)
                        closeTo(12, 0.001), // (1 * 12)
                        // (2 * 3)
                        closeTo(8, 0.001) // (3 * 8)
                        // (4 * 2)
                )));
    }

    @Test
    @SuppressWarnings("unchecked")
    public void testFilterIndexedWithStartAndStep() {
        DoubleStream.of(1, 12, 3, 8, 2)
                .filterIndexed(4, -2, new IndexedDoublePredicate() {
                    @Override
                    public boolean test(int index, double value) {
                        return Math.abs(index * value) > 10;
                    }
                })
                .custom(assertElements(arrayContaining(
                        // (4 * 1)
                        closeTo(12, 0.001), // (2 * 12)
                        // (0 * 3)
                        closeTo(8, 0.001) // (-2 * 8)
                        // (-4 * 2)
                )));
    }

    @Test(expected = NoSuchElementException.class)
    public void testFilterIndexedIteratorNextOnEmpty() {
        DoubleStream.empty()
                .filterIndexed(IndexedDoublePredicate.Util.wrap(Functions.greaterThan(Math.PI)))
                .iterator()
                .next();
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testFilterIndexedIteratorRemove() {
        DoubleStream.of(1, 2, 3, 4, 5)
                .filterIndexed(IndexedDoublePredicate.Util.wrap(Functions.greaterThan(Math.PI)))
                .iterator()
                .remove();
    }
}
