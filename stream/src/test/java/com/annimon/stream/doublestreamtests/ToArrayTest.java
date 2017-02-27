package com.annimon.stream.doublestreamtests;

import com.annimon.stream.DoubleStream;
import org.junit.Test;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public final class ToArrayTest {

    @Test
    public void testToArray() {
        assertThat(DoubleStream.of(0.012, 10.347, 3.039, 19.84, 100d).toArray(),
                is(new double[] {0.012, 10.347, 3.039, 19.84, 100d}));
    }

    @Test
    public void testToArrayOnEmptyStream() {
        assertThat(DoubleStream.empty().toArray().length, is(0));
    }
}
