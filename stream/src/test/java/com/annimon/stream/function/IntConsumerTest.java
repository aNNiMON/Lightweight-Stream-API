package com.annimon.stream.function;

import java.util.ArrayList;
import java.util.List;
import org.junit.Test;
import static com.annimon.stream.test.hamcrest.CommonMatcher.hasOnlyPrivateConstructors;
import static org.hamcrest.Matchers.contains;
import static org.junit.Assert.assertThat;

/**
 * Tests {@code IntConsumer}.
 *
 * @see com.annimon.stream.function.IntConsumer
 */
public class IntConsumerTest {

    @Test
    public void testAndThen() {
        final List<Integer> buffer = new ArrayList<Integer>();
        IntConsumer consumer = IntConsumer.Util.andThen(
                new Increment(buffer), new Multiplier(buffer, 2));

        // (10+1) (10*2)
        consumer.accept(10);
        assertThat(buffer, contains(11, 20));
        buffer.clear();

        // (22+1) (22*2)
        consumer.accept(22);
        assertThat(buffer, contains(23, 44));
        buffer.clear();

        consumer.accept(-10);
        consumer.accept(5);
        consumer.accept(118);
        assertThat(buffer, contains(-9, -20,  6, 10,  119, 236));
    }

    @Test
    public void testPrivateConstructor() throws Exception {
        assertThat(IntConsumer.Util.class, hasOnlyPrivateConstructors());
    }

    private static class Increment implements IntConsumer {

        private final List<Integer> buffer;

        public Increment(List<Integer> buffer) {
            this.buffer = buffer;
        }

        @Override
        public void accept(int value) {
            value++;
            buffer.add(value);
        }
    }

    private static class Multiplier implements IntConsumer {

        private final int factor;
        private final List<Integer> buffer;

        public Multiplier(List<Integer> buffer, int factor) {
            this.buffer = buffer;
            this.factor = factor;
        }

        @Override
        public void accept(int value) {
            value *= factor;
            buffer.add(value);
        }
    }

}
