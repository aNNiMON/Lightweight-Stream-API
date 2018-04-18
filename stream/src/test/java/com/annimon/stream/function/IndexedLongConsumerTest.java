package com.annimon.stream.function;

import org.junit.Test;

import static com.annimon.stream.test.hamcrest.CommonMatcher.hasOnlyPrivateConstructors;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

/**
 * Tests {@code IndexedLongConsumer}.
 *
 * @see IndexedLongConsumer
 */
public class IndexedLongConsumerTest {

    @Test
    public void testPrivateConstructor() throws Exception {
        assertThat(IndexedLongConsumer.Util.class, hasOnlyPrivateConstructors());
    }

    @Test
    public void testAndThen() {
        final long[] buffer = new long[] {1L, 2L, 4L, 8L};
        IndexedLongConsumer addConsumer = new IndexedLongConsumer() {
            @Override
            public void accept(int index, long value) {
                buffer[index] += value;
            }
        };
        IndexedLongConsumer multiplyConsumer = new IndexedLongConsumer() {
            @Override
            public void accept(int index, long value) {
                buffer[index] *= value;
            }
        };
        IndexedLongConsumer consumer = IndexedLongConsumer.Util.andThen(
                addConsumer, multiplyConsumer);

        // 2 + 4 = 6; 6 * 4 = 24
        consumer.accept(1, 4L);
        assertEquals(24L, buffer[1]);

        // 4 + (-8) = -4; -4 * (-8) = 32
        consumer.accept(2, -8L);
        assertEquals(32L, buffer[2]);
    }

    @Test
    public void testUtilAccept() {
        final IntHolder countHolder = new IntHolder();
        final LongHolder valueHolder = new LongHolder(10L);
        final IntConsumer indexConsumer = new IntConsumer() {
            @Override
            public void accept(int index) {
                countHolder.value++;
            }
        };
        IndexedLongConsumer consumer = IndexedLongConsumer.Util
                .accept(indexConsumer, valueHolder);

        for (int i = 1; i < 11; i++) {
            consumer.accept(i, (long) i);
            assertEquals(i, countHolder.value);
        }
        assertEquals(65, valueHolder.value);
    }


    private static class IntHolder {
        public int value;
    }

    private static class LongHolder implements LongConsumer {

        public long value;

        LongHolder(long value) {
            this.value = value;
        }

        @Override
        public void accept(long value) {
            this.value += value;
        }
    }
}
