package com.annimon.stream.longstreamtests;

import com.annimon.stream.LongStream;
import org.junit.Test;
import static com.annimon.stream.test.hamcrest.OptionalLongMatcher.hasValue;
import static com.annimon.stream.test.hamcrest.OptionalLongMatcher.isEmpty;
import static org.junit.Assert.assertThat;

public final class MinTest {

    @Test
    public void testMin() {
        assertThat(LongStream.of(100, 20, 3).min(), hasValue(3L));
        assertThat(LongStream.empty().min(), isEmpty());
    }
}
