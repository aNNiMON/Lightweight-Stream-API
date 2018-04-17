package com.annimon.stream.function;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import org.junit.BeforeClass;
import org.junit.Test;
import static com.annimon.stream.test.hamcrest.CommonMatcher.hasOnlyPrivateConstructors;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

/**
 * Tests {@code LongSupplier}.
 *
 * @see LongSupplier
 */
public class LongSupplierTest {

    private static byte[] input;

    @BeforeClass
    public static void setUp() throws IOException {
        final ByteArrayOutputStream baos = new ByteArrayOutputStream(15);
        final DataOutputStream dos = new DataOutputStream(baos);
        dos.writeLong(10L);
        dos.writeLong(15L);
        dos.writeLong(20L);
        input = baos.toByteArray();
    }

    @Test
    public void testPrivateConstructor() throws Exception {
        assertThat(LongSupplier.Util.class, hasOnlyPrivateConstructors());
    }

    @Test
    public void testSafe() throws IOException {
        DataInputStream dis = new DataInputStream(new ByteArrayInputStream(input));

        LongSupplier supplier = LongSupplier.Util.safe(new UnsafeSupplier(dis));
        assertThat(supplier.getAsLong(), is(10L));
        assertThat(supplier.getAsLong(), is(15L));
        assertThat(supplier.getAsLong(), is(20L));
        assertThat(supplier.getAsLong(), is(0L));
        assertThat(supplier.getAsLong(), is(0L));
    }

    @Test
    public void testSafeWithOnFailedSupplier() throws IOException {
        DataInputStream dis = new DataInputStream(new ByteArrayInputStream(input));

        LongSupplier supplier = LongSupplier.Util.safe(new UnsafeSupplier(dis), 500L);
        assertThat(supplier.getAsLong(), is(10L));
        assertThat(supplier.getAsLong(), is(15L));
        assertThat(supplier.getAsLong(), is(20L));
        assertThat(supplier.getAsLong(), is(500L));
        assertThat(supplier.getAsLong(), is(500L));
    }

    private static class UnsafeSupplier implements ThrowableLongSupplier<Throwable> {

        private final DataInputStream is;

        UnsafeSupplier(DataInputStream is) {
            this.is = is;
        }

        @Override
        public long getAsLong() throws IOException {
            return is.readLong();
        }
    }
}
