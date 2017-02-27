package com.annimon.stream.doublestreamtests;

import com.annimon.stream.DoubleStream;
import com.annimon.stream.test.hamcrest.OptionalDoubleMatcher;
import org.junit.Test;
import static org.hamcrest.Matchers.closeTo;
import static org.junit.Assert.assertThat;

public final class AverageTest {

    @Test
    public void testAverage() {
        assertThat(DoubleStream.of(0.1, 0.02, 0.003).average(),
                OptionalDoubleMatcher.hasValueThat(closeTo(0.041, 0.00001)));

        assertThat(DoubleStream.empty().average(), OptionalDoubleMatcher.isEmpty());
    }
}
