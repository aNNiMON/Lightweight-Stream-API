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
        assertThat(buffer, contains(-9, -20, 6, 10, 119, 236));
    }

    @Test
    public void testSafe() throws IOException {
        final ByteArrayOutputStream baos = new ByteArrayOutputStream(15);
        final DataOutputStream dos = new DataOutputStream(baos);
        IntConsumer consumer = IntConsumer.Util.safe(new UnsafeConsumer(dos));

        consumer.accept(10);
        consumer.accept(20);
        consumer.accept(-5);
        consumer.accept(-8);
        consumer.accept(500);

        final byte[] result = baos.toByteArray();
        DataInputStream dis = new DataInputStream(new ByteArrayInputStream(result));
        assertThat(dis.readInt(), is(10));
        assertThat(dis.readInt(), is(20));
        assertThat(dis.readInt(), is(500));
    }

    @Test
    public void testSafeWithOnFailedConsumer() throws IOException {
        final ByteArrayOutputStream baos = new ByteArrayOutputStream(15);
        final DataOutputStream dos = new DataOutputStream(baos);
        IntConsumer consumer = IntConsumer.Util.safe(new UnsafeConsumer(dos), new IntConsumer() {
            @Override
            public void accept(int value) {
                baos.write(0);
            }
        });

        consumer.accept(10);
        consumer.accept(20);
        consumer.accept(-5);
        consumer.accept(-8);
        consumer.accept(500);

        final byte[] result = baos.toByteArray();
        DataInputStream dis = new DataInputStream(new ByteArrayInputStream(result));
        assertThat(dis.readInt(), is(10));
        assertThat(dis.readInt(), is(20));
        assertThat(dis.readByte(), is((byte) 0));
        assertThat(dis.readByte(), is((byte) 0));
        assertThat(dis.readInt(), is(500));
    }

    @Test
    public void testPrivateConstructor() throws Exception {
        assertThat(IntConsumer.Util.class, hasOnlyPrivateConstructors());
    }

    private static class Increment implements IntConsumer {

        private final List<Integer> buffer;

        Increment(List<Integer> buffer) {
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

        Multiplier(List<Integer> buffer, int factor) {
            this.buffer = buffer;
            this.factor = factor;
        }

        @Override
        public void accept(int value) {
            value *= factor;
            buffer.add(value);
        }
    }

    private static class UnsafeConsumer implements ThrowableIntConsumer<Throwable> {

        private final DataOutputStream os;

        UnsafeConsumer(DataOutputStream os) {
            this.os = os;
        }

        @Override
        public void accept(int value) throws IOException {
            if (value < 0) {
                throw new IOException();
            }
            os.writeInt(value);
        }
    }
}
