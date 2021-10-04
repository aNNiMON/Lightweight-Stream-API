package com.annimon.stream.longstreamtests;

import static com.annimon.stream.test.hamcrest.OptionalLongMatcher.hasValue;
import static com.annimon.stream.test.hamcrest.OptionalLongMatcher.isEmpty;
import static org.hamcrest.MatcherAssert.assertThat;

import com.annimon.stream.LongStream;
import org.junit.Test;

public final class FindFirstTest {

    @Test
    public void testFindFirst() {
        assertThat(LongStream.of(3, 10, 19, 4, 50).findFirst(), hasValue(3L));

        assertThat(LongStream.empty().findFirst(), isEmpty());
    }
}
