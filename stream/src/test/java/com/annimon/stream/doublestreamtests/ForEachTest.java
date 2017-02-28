package com.annimon.stream.doublestreamtests;

import com.annimon.stream.DoubleStream;
import com.annimon.stream.function.DoubleConsumer;
import org.junit.Test;
import static org.hamcrest.Matchers.closeTo;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

public final class ForEachTest {

    @Test
    public void testForEach() {
        final double[] expected = {1.2, 3.456};
        DoubleStream.of(1.2, 3.456).forEach(new DoubleConsumer() {

            private int index = 0;

            @Override
            public void accept(double value) {
                assertThat(value, closeTo(expected[index++], 0.0001));
            }
        });
    }

    @Test
    public void testForEachOnEmptyStream() {
        DoubleStream.empty().forEach(new DoubleConsumer() {
            @Override
            public void accept(double value) {
                fail();
            }
        });
    }
}
