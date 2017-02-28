package com.annimon.stream.doublestreamtests;

import com.annimon.stream.DoubleStream;
import java.util.Comparator;
import org.junit.Test;
import static com.annimon.stream.test.hamcrest.DoubleStreamMatcher.assertElements;
import static com.annimon.stream.test.hamcrest.DoubleStreamMatcher.isEmpty;
import static org.hamcrest.Matchers.arrayContaining;
import static org.junit.Assert.assertThat;

public final class SortedTest {

    @Test
    public void testSorted() {
        DoubleStream.of(1.2, 3.234, 0.09, 2.2)
                .sorted()
                .custom(assertElements(arrayContaining(
                        0.09, 1.2, 2.2, 3.234
                )));
    }

    @Test
    public void testSortedOnEmptyStream() {
        assertThat(DoubleStream.empty().sorted(),
                isEmpty());
    }

    @Test
    public void testSortedWithComparator() {
        DoubleStream.of(1.2, 3.234, 0.09, 2.2)
                .sorted(new Comparator<Double>() {
                    @Override
                    public int compare(Double o1, Double o2) {
                        // reverse order
                        return Double.compare(o2, o1);
                    }
                })
                .custom(assertElements(arrayContaining(
                        3.234, 2.2, 1.2, 0.09
                )));
    }
}
