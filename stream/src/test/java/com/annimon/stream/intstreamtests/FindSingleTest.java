package com.annimon.stream.intstreamtests;

import com.annimon.stream.Functions;
import com.annimon.stream.IntStream;
import com.annimon.stream.OptionalInt;
import org.junit.Test;
import static com.annimon.stream.test.hamcrest.OptionalIntMatcher.hasValue;
import static com.annimon.stream.test.hamcrest.OptionalIntMatcher.isEmpty;
import static org.junit.Assert.assertThat;

public final class FindSingleTest {

    @Test
    public void testFindSingleOnEmptyStream() {
        assertThat(IntStream.empty().findSingle(), isEmpty());
    }

    @Test
    public void testFindSingleOnOneElementStream() {
        OptionalInt result = IntStream.of(42).findSingle();

        assertThat(result, hasValue(42));
    }

    @Test(expected = IllegalStateException.class)
    public void testFindSingleOnMoreElementsStream() {
        IntStream.rangeClosed(1, 2).findSingle();
    }

    @Test
    public void testFindSingleAfterFilteringToEmptyStream() {
        OptionalInt result = IntStream.range(1, 5)
                .filter(Functions.remainderInt(6))
                .findSingle();

        assertThat(result, isEmpty());
    }

    @Test
    public void testFindSingleAfterFilteringToOneElementStream() {
        OptionalInt result = IntStream.range(1, 10)
                .filter(Functions.remainderInt(6))
                .findSingle();

        assertThat(result, hasValue(6));
    }

    @Test(expected = IllegalStateException.class)
    public void testFindSingleAfterFilteringToMoreElementStream() {
        IntStream.range(1, 100)
                .filter(Functions.remainderInt(6))
                .findSingle();
    }
}
