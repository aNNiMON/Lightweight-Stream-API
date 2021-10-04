package com.annimon.stream.doublestreamtests;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import com.annimon.stream.DoubleStream;
import org.junit.Test;

public final class ToArrayTest {

    @Test
    public void testToArray() {
        assertThat(
                DoubleStream.of(0.012, 10.347, 3.039, 19.84, 100d).toArray(),
                is(new double[] {0.012, 10.347, 3.039, 19.84, 100d}));
    }

    @Test
    public void testToArrayOnEmptyStream() {
        assertThat(DoubleStream.empty().toArray().length, is(0));
    }
}
