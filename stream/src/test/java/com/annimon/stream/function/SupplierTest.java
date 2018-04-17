package com.annimon.stream.function;

import com.annimon.stream.Functions;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import org.junit.BeforeClass;
import org.junit.Test;
import static com.annimon.stream.test.hamcrest.CommonMatcher.hasOnlyPrivateConstructors;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

/**
 * Tests {@code Supplier}.
 *
 * @see com.annimon.stream.function.Supplier
 */
public class SupplierTest {

    private static byte[] input;

    @BeforeClass
    public static void setUp() throws IOException {
        final ByteArrayOutputStream baos = new ByteArrayOutputStream(15);
        final DataOutputStream dos = new DataOutputStream(baos);
        dos.writeUTF("test");
        dos.writeUTF("123");
        input = baos.toByteArray();
    }

    @Test
    public void testPrivateConstructor() throws Exception {
        assertThat(Supplier.Util.class, hasOnlyPrivateConstructors());
    }

    @Test
    public void testGetString() {
        Supplier<String> supplier = new Supplier<String>() {

            @Override
            public String get() {
                return "fantastic";
            }
        };
        assertEquals("fantastic", supplier.get());
    }

    @Test
    public void testGetStringBuilder() {
        Supplier<StringBuilder> supplier = Functions.stringBuilderSupplier();

        assertThat(supplier.get(), instanceOf(StringBuilder.class));
        assertTrue(supplier.get().toString().isEmpty());

    }

    @Test
    public void testIncrement() {
        Supplier<Integer> supplier = new Supplier<Integer>() {
            private int counter = 0;

            @Override
            public Integer get() {
                return counter++;
            }
        };
        assertEquals(0, supplier.get().intValue());
        assertEquals(1, supplier.get().intValue());
        assertEquals(2, supplier.get().intValue());
        assertEquals(3, supplier.get().intValue());
    }

    @Test
    public void testSafe() throws IOException {
        DataInputStream dis = new DataInputStream(new ByteArrayInputStream(input));

        Supplier<String> supplier = Supplier.Util.safe(new UnsafeSupplier(dis));
        assertThat(supplier.get(), is("test"));
        assertThat(supplier.get(), is("123"));
        assertThat(supplier.get(), is(nullValue()));
        assertThat(supplier.get(), is(nullValue()));
    }

    @Test
    public void testSafeWithOnFailedSupplier() throws IOException {
        DataInputStream dis = new DataInputStream(new ByteArrayInputStream(input));

        Supplier<String> supplier = Supplier.Util.safe(new UnsafeSupplier(dis), "oops");
        assertThat(supplier.get(), is("test"));
        assertThat(supplier.get(), is("123"));
        assertThat(supplier.get(), is("oops"));
        assertThat(supplier.get(), is("oops"));
    }

    private static class UnsafeSupplier implements ThrowableSupplier<String, Throwable> {

        private final DataInputStream is;

        UnsafeSupplier(DataInputStream is) {
            this.is = is;
        }

        @Override
        public String get() throws IOException {
            return is.readUTF();
        }
    }
}
