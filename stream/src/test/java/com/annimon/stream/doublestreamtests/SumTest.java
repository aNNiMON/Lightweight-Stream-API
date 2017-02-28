package com.annimon.stream.doublestreamtests;

import com.annimon.stream.DoubleStream;
import org.junit.Test;
import static org.hamcrest.Matchers.closeTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public final class SumTest {

    @Test
    public void testSum() {
        assertThat(DoubleStream.of(0.1, 0.02, 0.003).sum(), closeTo(0.123, 0.0001));
        assertThat(DoubleStream.empty().sum(), is(0d));
    }
}
