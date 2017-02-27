package com.annimon.stream.doublestreamtests;

import com.annimon.stream.DoubleStream;
import org.junit.Test;
import static org.hamcrest.Matchers.closeTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public final class IteratorTest {

    @Test
    public void testIterator() {
        assertThat(DoubleStream.of(1.0123).iterator().nextDouble(), closeTo(1.0123, 0.0001));

        assertThat(DoubleStream.empty().iterator().hasNext(), is(false));

        assertThat(DoubleStream.empty().iterator().nextDouble(), is(0d));
    }
}
