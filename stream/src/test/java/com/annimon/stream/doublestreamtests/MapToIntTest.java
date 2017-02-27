package com.annimon.stream.doublestreamtests;

import com.annimon.stream.DoubleStream;
import com.annimon.stream.function.DoubleToIntFunction;
import org.junit.Test;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public final class MapToIntTest {

    @Test
    public void testMapToInt() {
        DoubleToIntFunction mapper = new DoubleToIntFunction() {
            @Override
            public int applyAsInt(double value) {
                return (int) (value * 10);
            }
        };
        assertThat(DoubleStream.of(0.2, 0.3, 0.4).mapToInt(mapper).toArray(),
                is(new int[] {2, 3, 4}));
    }
}
