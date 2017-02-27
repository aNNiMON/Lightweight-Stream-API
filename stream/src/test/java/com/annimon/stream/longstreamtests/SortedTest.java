package com.annimon.stream.longstreamtests;

import com.annimon.stream.LongStream;
import java.util.Comparator;
import org.junit.Test;
import static com.annimon.stream.test.hamcrest.LongStreamMatcher.elements;
import static com.annimon.stream.test.hamcrest.LongStreamMatcher.isEmpty;
import static org.hamcrest.Matchers.arrayContaining;
import static org.junit.Assert.assertThat;

public final class SortedTest {

    @Test
    public void testSorted() {
        assertThat(LongStream.of(12, 32, 9, 22).sorted(),
                elements(arrayContaining(9L, 12L, 22L, 32L)));

        assertThat(LongStream.empty().sorted(),
                isEmpty());
    }

    @Test
    public void testSortedWithComparator() {
        assertThat(LongStream.of(12, 32, 9, 22).sorted(new Comparator<Long>() {
            @Override
            public int compare(Long o1, Long o2) {
                // reverse order
                return Long.compare(o2, o1);
            }
        }), elements(arrayContaining(32L, 22L, 12L, 9L)));
    }
}
