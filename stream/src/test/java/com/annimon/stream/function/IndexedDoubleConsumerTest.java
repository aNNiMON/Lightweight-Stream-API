package com.annimon.stream.function;

import org.junit.Test;

import static com.annimon.stream.test.hamcrest.CommonMatcher.hasOnlyPrivateConstructors;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

/**
 * Tests {@code IndexedDoubleConsumer}.
 *
 * @see IndexedDoubleConsumer
 */
public class IndexedDoubleConsumerTest {

    @Test
    public void testPrivateConstructor() throws Exception {
        assertThat(IndexedDoubleConsumer.Util.class, hasOnlyPrivateConstructors());
    }

    @Test
    public void testAndThen() {
        final double[] buffer = new double[] {1.0, 2.5, 4.0, 2.0};
        IndexedDoubleConsumer addConsumer = new IndexedDoubleConsumer() {
            @Override
            public void accept(int index, double value) {
                buffer[index] += value;
            }
        };
        IndexedDoubleConsumer multiplyConsumer = new IndexedDoubleConsumer() {
            @Override
            public void accept(int index, double value) {
                buffer[index] *= value;
            }
        };
        IndexedDoubleConsumer consumer = IndexedDoubleConsumer.Util.andThen(
                addConsumer, multiplyConsumer);

        // 2.5 + 2.5 = 5.0; 5.0 * 2.5 = 12.5
        consumer.accept(1, 2.5);
        assertEquals(12.5, buffer[1], 0.0);

        // 4.0 + (-6.0) = -2.0; -2.0 * (-6.0) = 12.0
        consumer.accept(2, -6.0);
        assertEquals(12.0, buffer[2], 0.0);
    }

    @Test
    public void testUtilAccept() {
        final IntHolder countHolder = new IntHolder();
        final DoubleHolder valueHolder = new DoubleHolder(10.0);
        final IntConsumer indexConsumer = new IntConsumer() {
            @Override
            public void accept(int index) {
                countHolder.value++;
            }
        };
        IndexedDoubleConsumer consumer = IndexedDoubleConsumer.Util
                .accept(indexConsumer, valueHolder);

        for (int i = 1; i < 11; i++) {
            consumer.accept(i, (double) i);
            assertEquals(i, countHolder.value);
        }
        assertEquals(65.0, valueHolder.value, 0.0);
    }


    private static class IntHolder {
        public int value;
    }

    private static class DoubleHolder implements DoubleConsumer {

        public double value;

        DoubleHolder(double value) {
            this.value = value;
        }

        @Override
        public void accept(double value) {
            this.value += value;
        }
    }
}
