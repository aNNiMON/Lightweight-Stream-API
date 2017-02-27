package com.annimon.stream.doublestreamtests;

import com.annimon.stream.DoubleStream;
import com.annimon.stream.function.DoubleToLongFunction;
import org.junit.Test;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public final class MapToLongTest {

    @Test
    public void testMapToLong() {
        DoubleToLongFunction mapper = new DoubleToLongFunction() {
            @Override
            public long applyAsLong(double value) {
                return (long) (value * 10000000000L);
            }
        };
        assertThat(DoubleStream.of(0.2, 0.3, 0.004).mapToLong(mapper).toArray(),
                is(new long[] {2000000000L, 3000000000L, 40000000L}));
    }
}
