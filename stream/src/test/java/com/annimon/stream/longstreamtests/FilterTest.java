package com.annimon.stream.longstreamtests;

import com.annimon.stream.Functions;
import com.annimon.stream.LongStream;
import com.annimon.stream.function.LongPredicate;
import org.junit.Test;
import static com.annimon.stream.test.hamcrest.LongStreamMatcher.elements;
import static com.annimon.stream.test.hamcrest.LongStreamMatcher.isEmpty;
import static org.hamcrest.Matchers.arrayContaining;
import static org.junit.Assert.assertThat;

public final class FilterTest {

    @Test
    public void testFilter() {
        final LongPredicate predicate = Functions.remainderLong(111);
        assertThat(LongStream.of(322, 555, 666, 1984, 1998).filter(predicate),
                elements(arrayContaining(555L, 666L, 1998L)));

        assertThat(LongStream.of(12, -10).filter(predicate),
                isEmpty());
    }
}
