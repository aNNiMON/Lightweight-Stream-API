package com.annimon.stream.function;

import static com.annimon.stream.test.hamcrest.CommonMatcher.hasOnlyPrivateConstructors;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;

import java.util.ArrayList;
import java.util.List;
import org.junit.Test;

/**
 * Tests {@code BooleanConsumer}.
 *
 * @see BooleanConsumer
 */
public class BooleanConsumerTest {

    @Test
    public void testPrivateConstructor() throws Exception {
        assertThat(BooleanConsumer.Util.class, hasOnlyPrivateConstructors());
    }

    @Test
    public void testAndThen() {
        final List<Boolean> buffer = new ArrayList<Boolean>();
        BooleanConsumer consumer =
                BooleanConsumer.Util.andThen(
                        new BooleanOperator(buffer, false), new BooleanOperator(buffer, true));

        // (false && false) (false || false)
        // (false && true) (false || true)
        consumer.accept(false);
        assertThat(buffer, contains(false, false, false, true));
        buffer.clear();

        // (true && false) (true || false)
        // (true && true) (true || true)
        consumer.accept(true);
        assertThat(buffer, contains(false, true, true, true));
        buffer.clear();

        consumer.accept(true);
        consumer.accept(false);
        consumer.accept(true);
        assertThat(
                buffer,
                contains(
                        false, true, true, true, false, false, false, true, false, true, true,
                        true));
    }

    private static class BooleanOperator implements BooleanConsumer {

        private final boolean factor;
        private final List<Boolean> buffer;

        BooleanOperator(List<Boolean> buffer, boolean factor) {
            this.buffer = buffer;
            this.factor = factor;
        }

        @Override
        public void accept(boolean value) {
            buffer.add(value && factor);
            buffer.add(value || factor);
        }
    }
}
