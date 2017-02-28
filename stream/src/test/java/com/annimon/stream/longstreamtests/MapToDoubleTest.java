package com.annimon.stream.longstreamtests;

import com.annimon.stream.DoubleStream;
import com.annimon.stream.LongStream;
import com.annimon.stream.function.LongToDoubleFunction;
import com.annimon.stream.test.hamcrest.DoubleStreamMatcher;
import org.junit.Test;
import static org.hamcrest.Matchers.array;
import static org.hamcrest.Matchers.closeTo;
import static org.junit.Assert.assertThat;

public final class MapToDoubleTest {

    @Test
    @SuppressWarnings("unchecked")
    public void testMapToDouble() {
        DoubleStream stream = LongStream.rangeClosed(2, 4)
                .mapToDouble(new LongToDoubleFunction() {
                    @Override
                    public double applyAsDouble(long value) {
                        return value / 10d;
                    }
                });
        assertThat(stream, DoubleStreamMatcher.elements(array(
                closeTo(0.2, 0.00001),
                closeTo(0.3, 0.00001),
                closeTo(0.4, 0.00001)
        )));
    }
}
