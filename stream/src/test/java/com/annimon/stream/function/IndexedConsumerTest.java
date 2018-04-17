package com.annimon.stream.function;

import org.junit.Test;
import static com.annimon.stream.test.hamcrest.CommonMatcher.hasOnlyPrivateConstructors;
import static org.junit.Assert.*;

/**
 * Tests {@code IndexedConsumer}.
 *
 * @see com.annimon.stream.function.IndexedConsumer
 */
public class IndexedConsumerTest {

    @Test
    public void testPrivateConstructor() throws Exception {
        assertThat(IndexedConsumer.Util.class, hasOnlyPrivateConstructors());
    }

    @Test
    public void testAccept() {
        IntHolder holder = new IntHolder(10);

        adder.accept(1, holder);
        assertEquals(11, holder.value);

        adder.accept(31, holder);
        assertEquals(42, holder.value);
    }

    @Test
    public void testUtilWrap() {
        IndexedConsumer<IntHolder> increment = IndexedConsumer.Util
                .wrap(new Consumer<IntHolder>() {
                    @Override
                    public void accept(IntHolder t) {
                        t.value++;
                    }
                });
        IntHolder holder = new IntHolder(10);

        increment.accept(0, holder);
        assertEquals(11, holder.value);

        increment.accept(20, holder);
        assertEquals(12, holder.value);
    }

    @Test
    public void testUtilAccept() {
        final IntHolder holder = new IntHolder(0);
        final IntConsumer intConsumer = new IntConsumer() {
            @Override
            public void accept(int value) {
                holder.value -= value;
            }
        };
        final Consumer<IntHolder> objectConsumer = new Consumer<IntHolder>() {
            @Override
            public void accept(IntHolder t) {
                t.value++;
            }
        };

        holder.value = 10;
        IndexedConsumer<IntHolder> consumer = IndexedConsumer.Util
                .accept(intConsumer, objectConsumer);
        // 10 - 0 + 1
        consumer.accept(0, holder);
        assertEquals(11, holder.value);

        // 11 - 5 + 1
        consumer.accept(5, holder);
        assertEquals(7, holder.value);


        holder.value = 10;
        IndexedConsumer<IntHolder> consumerObject = IndexedConsumer.Util
                .accept(null, objectConsumer);
        // 10 + 1
        consumerObject.accept(0, holder);
        assertEquals(11, holder.value);

        // 11 + 1
        consumerObject.accept(5, holder);
        assertEquals(12, holder.value);


        holder.value = 10;
        IndexedConsumer<IntHolder> consumerIndex = IndexedConsumer.Util
                .accept(intConsumer, null);
        // 10 - 0
        consumerIndex.accept(0, holder);
        assertEquals(10, holder.value);

        // 10 - 5
        consumerIndex.accept(5, holder);
        assertEquals(5, holder.value);
    }

    private static final IndexedConsumer<IntHolder>
            adder = new IndexedConsumer<IntHolder>() {
        @Override
        public void accept(int index, IntHolder holder) {
            holder.value += index;
        }
    };

    static class IntHolder {
        int value;

        IntHolder(int value) {
            this.value = value;
        }
    }
}
