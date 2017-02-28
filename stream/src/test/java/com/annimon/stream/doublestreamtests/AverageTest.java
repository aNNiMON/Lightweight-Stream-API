package com.annimon.stream.doublestreamtests;

import com.annimon.stream.DoubleStream;
import org.junit.Test;
import static com.annimon.stream.test.hamcrest.OptionalDoubleMatcher.hasValueThat;
import static com.annimon.stream.test.hamcrest.OptionalDoubleMatcher.isEmpty;
import static org.hamcrest.Matchers.closeTo;
import static org.junit.Assert.assertThat;

public final class AverageTest {

    @Test
    public void testAverage() {
        assertThat(
                DoubleStream.of(0.1, 0.02, 0.003).average(),
                hasValueThat(closeTo(0.041, 0.00001)));

        assertThat(DoubleStream.empty().average(), isEmpty());
    }
}
