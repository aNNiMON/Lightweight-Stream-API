package com.annimon.stream.intstreamtests;

import com.annimon.stream.IntStream;
import org.junit.Test;
import static com.annimon.stream.test.hamcrest.OptionalIntMatcher.hasValue;
import static com.annimon.stream.test.hamcrest.OptionalIntMatcher.isEmpty;
import static org.junit.Assert.assertThat;

public final class FindLastTest {

    @Test
    public void testFindLast() {
        assertThat(IntStream.of(3, 10, 19, 4, 50).findLast(),
                hasValue(50));

        assertThat(IntStream.of(50).findLast(),
                hasValue(50));

        assertThat(IntStream.empty().findFirst(),
                isEmpty());
    }
}
