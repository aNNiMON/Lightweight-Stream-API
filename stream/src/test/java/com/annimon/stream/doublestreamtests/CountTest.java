package com.annimon.stream.doublestreamtests;

import com.annimon.stream.DoubleStream;
import org.junit.Test;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public final class CountTest {

    @Test
    public void testCount() {
        assertThat(DoubleStream.of(0.1, 0.02, 0.003).count(), is(3L));
        assertThat(DoubleStream.empty().count(), is(0L));
    }
}
