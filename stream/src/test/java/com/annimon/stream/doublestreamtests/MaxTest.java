package com.annimon.stream.doublestreamtests;

import com.annimon.stream.DoubleStream;
import com.annimon.stream.test.hamcrest.OptionalDoubleMatcher;
import org.junit.Test;
import static com.annimon.stream.test.hamcrest.OptionalDoubleMatcher.hasValue;
import static org.junit.Assert.assertThat;

public final class MaxTest {

    @Test
    public void testMax() {
        assertThat(DoubleStream.of(0.1, 0.02, 0.003).max(), hasValue(0.1));
        assertThat(DoubleStream.empty().max(), OptionalDoubleMatcher.isEmpty());
    }
}
