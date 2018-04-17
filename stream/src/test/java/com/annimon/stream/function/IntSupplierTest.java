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
 * Tests {@code IntSupplier}.
 *
 * @see IntSupplier
 */
public class IntSupplierTest {

    private static byte[] input;

    @BeforeClass
    public static void setUp() throws IOException {
        final ByteArrayOutputStream baos = new ByteArrayOutputStream(15);
        final DataOutputStream dos = new DataOutputStream(baos);
        dos.writeInt(10);
        dos.writeInt(15);
        dos.writeInt(20);
        input = baos.toByteArray();
    }

    @Test
    public void testPrivateConstructor() throws Exception {
        assertThat(IntSupplier.Util.class, hasOnlyPrivateConstructors());
    }

    @Test
    public void testSafe() throws IOException {
        DataInputStream dis = new DataInputStream(new ByteArrayInputStream(input));

        IntSupplier supplier = IntSupplier.Util.safe(new UnsafeSupplier(dis));
        assertThat(supplier.getAsInt(), is(10));
        assertThat(supplier.getAsInt(), is(15));
        assertThat(supplier.getAsInt(), is(20));
        assertThat(supplier.getAsInt(), is(0));
        assertThat(supplier.getAsInt(), is(0));
    }

    @Test
    public void testSafeWithOnFailedSupplier() throws IOException {
        DataInputStream dis = new DataInputStream(new ByteArrayInputStream(input));

        IntSupplier supplier = IntSupplier.Util.safe(new UnsafeSupplier(dis), 500);
        assertThat(supplier.getAsInt(), is(10));
        assertThat(supplier.getAsInt(), is(15));
        assertThat(supplier.getAsInt(), is(20));
        assertThat(supplier.getAsInt(), is(500));
        assertThat(supplier.getAsInt(), is(500));
    }

    private static class UnsafeSupplier implements ThrowableIntSupplier<Throwable> {

        private final DataInputStream is;

        UnsafeSupplier(DataInputStream is) {
            this.is = is;
        }

        @Override
        public int getAsInt() throws IOException {
            return is.readInt();
        }
    }
}
