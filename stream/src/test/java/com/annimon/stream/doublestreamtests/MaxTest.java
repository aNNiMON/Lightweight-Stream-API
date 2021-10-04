package com.annimon.stream.doublestreamtests;

import static com.annimon.stream.test.hamcrest.OptionalDoubleMatcher.hasValue;
import static com.annimon.stream.test.hamcrest.OptionalDoubleMatcher.isEmpty;
import static org.hamcrest.MatcherAssert.assertThat;

import com.annimon.stream.DoubleStream;
import org.junit.Test;

public final class MaxTest {

    @Test
    public void testMax() {
        assertThat(DoubleStream.of(0.1, 0.02, 0.003).max(), hasValue(0.1));
        assertThat(DoubleStream.empty().max(), isEmpty());
    }
}
