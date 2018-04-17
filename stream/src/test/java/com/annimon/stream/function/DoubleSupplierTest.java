package com.annimon.stream.function;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import org.junit.BeforeClass;
import org.junit.Test;
import static com.annimon.stream.test.hamcrest.CommonMatcher.hasOnlyPrivateConstructors;
import static org.hamcrest.Matchers.closeTo;
import static org.junit.Assert.assertThat;

/**
 * Tests {@code DoubleSupplier}.
 *
 * @see DoubleSupplier
 */
public class DoubleSupplierTest {

    private static byte[] input;

    @BeforeClass
    public static void setUp() throws IOException {
        final ByteArrayOutputStream baos = new ByteArrayOutputStream(15);
        final DataOutputStream dos = new DataOutputStream(baos);
        dos.writeDouble(0.16);
        dos.writeDouble(3.20);
        dos.writeDouble(5000);
        input = baos.toByteArray();
    }

    @Test
    public void testPrivateConstructor() throws Exception {
        assertThat(DoubleSupplier.Util.class, hasOnlyPrivateConstructors());
    }

    @Test
    public void testSafe() throws IOException {
        DataInputStream dis = new DataInputStream(new ByteArrayInputStream(input));

        DoubleSupplier supplier = DoubleSupplier.Util.safe(new UnsafeSupplier(dis));
        assertThat(supplier.getAsDouble(), closeTo(0.16, 0.0001));
        assertThat(supplier.getAsDouble(), closeTo(3.20, 0.0001));
        assertThat(supplier.getAsDouble(), closeTo(5000, 0.0001));
        assertThat(supplier.getAsDouble(), closeTo(0.0, 0.0001));
        assertThat(supplier.getAsDouble(), closeTo(0.0, 0.0001));
    }

    @Test
    public void testSafeWithOnFailedSupplier() throws IOException {
        DataInputStream dis = new DataInputStream(new ByteArrayInputStream(input));

        DoubleSupplier supplier = DoubleSupplier.Util.safe(new UnsafeSupplier(dis), 5000);
        assertThat(supplier.getAsDouble(), closeTo(0.16, 0.0001));
        assertThat(supplier.getAsDouble(), closeTo(3.20, 0.0001));
        assertThat(supplier.getAsDouble(), closeTo(5000, 0.0001));
        assertThat(supplier.getAsDouble(), closeTo(5000, 0.0001));
        assertThat(supplier.getAsDouble(), closeTo(5000, 0.0001));
    }

    private static class UnsafeSupplier implements ThrowableDoubleSupplier<Throwable> {

        private final DataInputStream is;

        UnsafeSupplier(DataInputStream is) {
            this.is = is;
        }

        @Override
        public double getAsDouble() throws IOException {
            return is.readDouble();
        }
    }
}
