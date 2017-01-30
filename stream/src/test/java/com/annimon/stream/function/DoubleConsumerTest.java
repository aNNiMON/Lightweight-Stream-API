package com.annimon.stream.function;

import java.util.ArrayList;
import java.util.List;
import org.junit.Test;
import static com.annimon.stream.test.hamcrest.CommonMatcher.hasOnlyPrivateConstructors;
import static org.hamcrest.Matchers.closeTo;
import static org.hamcrest.Matchers.contains;
import static org.junit.Assert.assertThat;

/**
 * Tests {@code DoubleConsumer}.
 *
 * @see com.annimon.stream.function.DoubleConsumer
 */
public class DoubleConsumerTest {

    @Test
    @SuppressWarnings("unchecked")
    public void testAndThen() {
        final List<Double> buffer = new ArrayList<Double>();
        DoubleConsumer consumer = DoubleConsumer.Util.andThen(
                new Multiplier(buffer, 0.1), new Multiplier(buffer, 2));

        consumer.accept(10d);
        assertThat(buffer, contains(
                closeTo(1, 0.0001),
                closeTo(20, 0.0001)
        ));
        buffer.clear();

        consumer.accept(22);
        assertThat(buffer, contains(
                closeTo(2.2, 0.0001),
                closeTo(44d, 0.0001)
        ));
        buffer.clear();

        consumer.accept(-10);
        consumer.accept(5);
        consumer.accept(0.08);
        assertThat(buffer, contains(
                closeTo(-1, 0.0001),
                closeTo(-20, 0.0001),
                closeTo(0.5, 0.0001),
                closeTo(10, 0.0001),
                closeTo(0.008, 0.0001),
                closeTo(0.16, 0.0001)
        ));
    }

    @Test
    public void testPrivateConstructor() throws Exception {
        assertThat(DoubleConsumer.Util.class, hasOnlyPrivateConstructors());
    }

    private static class Multiplier implements DoubleConsumer {

        private final double factor;
        private final List<Double> buffer;

        public Multiplier(List<Double> buffer, double factor) {
            this.buffer = buffer;
            this.factor = factor;
        }

        @Override
        public void accept(double value) {
            value *= factor;
            buffer.add(value);
        }
    }

}
