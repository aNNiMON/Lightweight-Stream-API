package com.annimon.stream.intstreamtests;

import static com.annimon.stream.test.hamcrest.OptionalIntMatcher.hasValue;
import static com.annimon.stream.test.hamcrest.OptionalIntMatcher.isEmpty;
import static org.hamcrest.MatcherAssert.assertThat;

import com.annimon.stream.IntStream;
import org.junit.Test;

public final class FindLastTest {

    @Test
    public void testFindLast() {
        assertThat(IntStream.of(3, 10, 19, 4, 50).findLast(), hasValue(50));

        assertThat(IntStream.of(50).findLast(), hasValue(50));

        assertThat(IntStream.empty().findFirst(), isEmpty());
    }
}
