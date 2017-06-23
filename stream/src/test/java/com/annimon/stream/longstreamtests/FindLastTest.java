package com.annimon.stream.longstreamtests;

import com.annimon.stream.LongStream;
import org.junit.Test;
import static com.annimon.stream.test.hamcrest.OptionalLongMatcher.hasValue;
import static com.annimon.stream.test.hamcrest.OptionalLongMatcher.isEmpty;
import static org.junit.Assert.assertThat;

public final class FindLastTest {

    @Test
    public void testFindLast() {
        assertThat(LongStream.of(3, 10, 19, 4, 50).findLast(),
                hasValue(50L));

        assertThat(LongStream.of(50).findLast(),
                hasValue(50L));

        assertThat(LongStream.empty().findLast(),
                isEmpty());
    }
}
