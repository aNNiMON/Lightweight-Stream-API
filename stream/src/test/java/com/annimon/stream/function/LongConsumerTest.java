package com.annimon.stream.function;

import java.util.ArrayList;
import java.util.List;
import org.junit.Test;
import static com.annimon.stream.test.hamcrest.CommonMatcher.hasOnlyPrivateConstructors;
import static org.hamcrest.Matchers.contains;
import static org.junit.Assert.assertThat;

/**
 * Tests {@code LongConsumer}.
 *
 * @see com.annimon.stream.function.LongConsumer
 */
public class LongConsumerTest {

    @Test
    public void testAndThen() {
        final List<Long> buffer = new ArrayList<Long>();
        LongConsumer consumer = LongConsumer.Util.andThen(
                new Multiplier(buffer, 10000000000L), new Multiplier(buffer, 2));

        // (10+1) (10*2)
        consumer.accept(10);
        assertThat(buffer, contains(100000000000L, 20L));
        buffer.clear();

        // (22+1) (22*2)
        consumer.accept(22);
        assertThat(buffer, contains(220000000000L, 44L));
        buffer.clear();

        consumer.accept(-10);
        consumer.accept(5);
        consumer.accept(118);
        assertThat(buffer, contains(
                -100000000000L, -20L,
                50000000000L, 10L,
                1180000000000L, 236L));
    }

    @Test
    public void testPrivateConstructor() throws Exception {
        assertThat(LongConsumer.Util.class, hasOnlyPrivateConstructors());
    }

    private class Multiplier implements LongConsumer {

        private final long factor;
        private final List<Long> buffer;

        public Multiplier(List<Long> buffer, long factor) {
            this.buffer = buffer;
            this.factor = factor;
        }

        @Override
        public void accept(long value) {
            value *= factor;
            buffer.add(value);
        }
    }

}
