package com.annimon.stream.doublestreamtests;

import com.annimon.stream.DoubleStream;
import com.annimon.stream.Functions;
import com.annimon.stream.function.DoublePredicate;
import java.util.NoSuchElementException;
import org.junit.Test;
import static com.annimon.stream.test.hamcrest.DoubleStreamMatcher.assertElements;
import static com.annimon.stream.test.hamcrest.DoubleStreamMatcher.assertIsEmpty;
import static org.hamcrest.Matchers.arrayContaining;

public final class FilterTest {

    @Test
    public void testFilter() {
        final DoublePredicate predicate = Functions.greaterThan(Math.PI);
        DoubleStream.of(0.012, 10.347, 3.039, 19.84, 100d)
                .filter(predicate)
                .custom(assertElements(arrayContaining(
                        10.347, 19.84, 100d
                )));

        DoubleStream.of(0.012, -10)
                .filter(predicate)
                .custom(assertIsEmpty());
    }

    @Test(expected = NoSuchElementException.class)
    public void testFilterIteratorNextOnEmpty() {
        DoubleStream.empty()
                .filter(Functions.greaterThan(Math.PI))
                .iterator()
                .next();
    }
}
