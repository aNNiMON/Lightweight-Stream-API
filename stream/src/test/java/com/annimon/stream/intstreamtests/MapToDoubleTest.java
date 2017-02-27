package com.annimon.stream.intstreamtests;

import com.annimon.stream.DoubleStream;
import com.annimon.stream.IntStream;
import com.annimon.stream.function.IntToDoubleFunction;
import com.annimon.stream.test.hamcrest.DoubleStreamMatcher;
import org.junit.Test;
import static org.hamcrest.Matchers.array;
import static org.hamcrest.Matchers.closeTo;
import static org.junit.Assert.assertThat;

public final class MapToDoubleTest {

    @Test
    @SuppressWarnings("unchecked")
    public void testMapToDouble() {
        DoubleStream stream = IntStream.rangeClosed(2, 4)
                .mapToDouble(new IntToDoubleFunction() {
                    @Override
                    public double applyAsDouble(int value) {
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
