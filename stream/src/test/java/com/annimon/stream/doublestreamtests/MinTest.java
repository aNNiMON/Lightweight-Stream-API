package com.annimon.stream.doublestreamtests;

import com.annimon.stream.DoubleStream;
import com.annimon.stream.test.hamcrest.OptionalDoubleMatcher;
import org.junit.Test;
import static com.annimon.stream.test.hamcrest.OptionalDoubleMatcher.hasValue;
import static org.junit.Assert.assertThat;

public final class MinTest {

    @Test
    public void testMin() {
        assertThat(DoubleStream.of(0.1, 0.02, 0.003).min(), hasValue(0.003));
        assertThat(DoubleStream.empty().min(), OptionalDoubleMatcher.isEmpty());
    }
}
