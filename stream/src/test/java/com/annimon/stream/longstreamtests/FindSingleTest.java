package com.annimon.stream.longstreamtests;

import com.annimon.stream.Functions;
import com.annimon.stream.LongStream;
import com.annimon.stream.OptionalLong;
import org.junit.Test;
import static com.annimon.stream.test.hamcrest.OptionalLongMatcher.hasValue;
import static com.annimon.stream.test.hamcrest.OptionalLongMatcher.isEmpty;
import static org.junit.Assert.assertThat;

public final class FindSingleTest {

    @Test
    public void testFindSingleOnEmptyStream() {
        assertThat(LongStream.empty().findSingle(),
                isEmpty());
    }

    @Test
    public void testFindSingleOnOneElementStream() {
        assertThat(LongStream.of(42L).findSingle(), hasValue(42L));
    }

    @Test(expected = IllegalStateException.class)
    public void testFindSingleOnMoreElementsStream() {
        LongStream.of(1, 2).findSingle();
    }

    @Test
    public void testFindSingleAfterFilteringToEmptyStream() {
        OptionalLong result = LongStream.of(5, 7, 9)
                .filter(Functions.remainderLong(2))
                .findSingle();

        assertThat(result, isEmpty());
    }

    @Test
    public void testFindSingleAfterFilteringToOneElementStream() {
        OptionalLong result = LongStream.of(5, 10, -15)
                .filter(Functions.remainderLong(2))
                .findSingle();

        assertThat(result, hasValue(10L));
    }

    @Test(expected = IllegalStateException.class)
    public void testFindSingleAfterFilteringToMoreElementStream() {
        LongStream.of(5, 10, -15, -20)
                .filter(Functions.remainderLong(2))
                .findSingle();
    }
}
