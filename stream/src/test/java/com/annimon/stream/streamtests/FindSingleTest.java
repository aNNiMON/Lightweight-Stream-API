package com.annimon.stream.streamtests;

import com.annimon.stream.Functions;
import com.annimon.stream.Optional;
import com.annimon.stream.Stream;
import org.junit.Test;
import static com.annimon.stream.test.hamcrest.OptionalMatcher.hasValue;
import static com.annimon.stream.test.hamcrest.OptionalMatcher.isEmpty;
import static org.junit.Assert.assertThat;

public final class FindSingleTest {

    @Test
    public void testFindSingleOnEmptyStream() {
        assertThat(Stream.empty().findSingle(), isEmpty());
    }

    @Test
    public void testFindSingleOnOneElementStream() {
        Optional<Integer> result = Stream.of(42).findSingle();

        assertThat(result, hasValue(42));
    }

    @Test(expected = IllegalStateException.class)
    public void testFindSingleOnMoreElementsStream() {
        Stream.rangeClosed(1, 2).findSingle();
    }

    @Test
    public void testFindSingleAfterFilteringToEmptyStream() {
        Optional<Integer> result = Stream.range(1, 5)
                .filter(Functions.remainder(6))
                .findSingle();

        assertThat(result, isEmpty());
    }

    @Test
    public void testFindSingleAfterFilteringToOneElementStream() {
        Optional<Integer> result = Stream.range(1, 10)
                .filter(Functions.remainder(6))
                .findSingle();

        assertThat(result, hasValue(6));
    }

    @Test(expected = IllegalStateException.class)
    public void testFindSingleAfterFilteringToMoreElementStream() {
        Stream.range(1, 100)
                .filter(Functions.remainder(6))
                .findSingle();
    }
}
