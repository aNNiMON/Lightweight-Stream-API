package com.annimon.stream.streamtests;

import com.annimon.stream.DoubleStream;
import com.annimon.stream.Stream;
import com.annimon.stream.function.ToDoubleFunction;
import org.junit.Test;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public final class MapToDoubleTest {

    @Test
    public void testMapToDouble() {
        final ToDoubleFunction<String> stringToDouble = new ToDoubleFunction<String>() {
            @Override
            public double applyAsDouble(String t) {
                return Double.parseDouble(t);
            }
        };
        double[] expected = { 1.23, 4.56789, 10.1112 };
        DoubleStream stream = Stream.of("1.23", "4.56789", "10.1112")
                .mapToDouble(stringToDouble);
        assertThat(stream.toArray(), is(expected));
    }
}
