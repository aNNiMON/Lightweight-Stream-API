package com.annimon.stream.doublestreamtests;

import com.annimon.stream.DoubleStream;
import com.annimon.stream.function.DoubleFunction;
import com.annimon.stream.test.hamcrest.StreamMatcher;
import java.util.Arrays;
import org.junit.Test;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public final class MapToObjTest {

    @Test
    public void testMapToObj() {
        DoubleFunction<String> doubleToString = new DoubleFunction<String>() {
            @Override
            public String apply(double value) {
                return Double.toString(value);
            }
        };
        assertThat(DoubleStream.of(1.0, 2.12, 3.234).mapToObj(doubleToString),
                StreamMatcher.elements(is(Arrays.asList("1.0", "2.12", "3.234"))));

        assertThat(DoubleStream.empty().mapToObj(doubleToString),
                StreamMatcher.isEmpty());
    }
}
