package com.annimon.stream.function;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.junit.Test;
import static com.annimon.stream.test.hamcrest.CommonMatcher.hasOnlyPrivateConstructors;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.is;
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
    public void testSafe() throws IOException {
        final ByteArrayOutputStream baos = new ByteArrayOutputStream(30);
        final DataOutputStream dos = new DataOutputStream(baos);
        LongConsumer consumer = LongConsumer.Util.safe(new UnsafeConsumer(dos));

        consumer.accept(10L);
        consumer.accept(20L);
        consumer.accept(-5L);
        consumer.accept(-8L);
        consumer.accept(500L);

        final byte[] result = baos.toByteArray();
        DataInputStream dis = new DataInputStream(new ByteArrayInputStream(result));
        assertThat(dis.readLong(), is(10L));
        assertThat(dis.readLong(), is(20L));
        assertThat(dis.readLong(), is(500L));
    }

    @Test
    public void testSafeWithOnFailedConsumer() throws IOException {
        final ByteArrayOutputStream baos = new ByteArrayOutputStream(30);
        final DataOutputStream dos = new DataOutputStream(baos);
        LongConsumer consumer = LongConsumer.Util.safe(new UnsafeConsumer(dos), new LongConsumer() {
            @Override
            public void accept(long value) {
                baos.write(0);
            }
        });

        consumer.accept(10L);
        consumer.accept(20L);
        consumer.accept(-5L);
        consumer.accept(-8L);
        consumer.accept(500L);

        final byte[] result = baos.toByteArray();
        DataInputStream dis = new DataInputStream(new ByteArrayInputStream(result));
        assertThat(dis.readLong(), is(10L));
        assertThat(dis.readLong(), is(20L));
        assertThat(dis.readByte(), is((byte) 0));
        assertThat(dis.readByte(), is((byte) 0));
        assertThat(dis.readLong(), is(500L));
    }

    @Test
    public void testPrivateConstructor() throws Exception {
        assertThat(LongConsumer.Util.class, hasOnlyPrivateConstructors());
    }

    private static class Multiplier implements LongConsumer {

        private final long factor;
        private final List<Long> buffer;

        Multiplier(List<Long> buffer, long factor) {
            this.buffer = buffer;
            this.factor = factor;
        }

        @Override
        public void accept(long value) {
            value *= factor;
            buffer.add(value);
        }
    }

    private static class UnsafeConsumer implements ThrowableLongConsumer<Throwable> {

        private final DataOutputStream os;

        UnsafeConsumer(DataOutputStream os) {
            this.os = os;
        }

        @Override
        public void accept(long value) throws IOException {
            if (value < 0) {
                throw new IOException();
            }
            os.writeLong(value);
        }
    }

}
