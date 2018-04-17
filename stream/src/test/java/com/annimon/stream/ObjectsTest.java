package com.annimon.stream;

import com.annimon.stream.function.Supplier;
import java.util.Arrays;
import java.util.Collection;
import java.util.Random;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import static com.annimon.stream.test.hamcrest.CommonMatcher.hasOnlyPrivateConstructors;
import static org.hamcrest.Matchers.contains;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

/**
 * Tests {@code Objects}.
 *
 * @see com.annimon.stream.Objects
 */
public class ObjectsTest {

    @Rule
    public final ExpectedException expectedException = ExpectedException.none();

    @Test
    public void testEqualsThisObjects() {
        assertTrue(Objects.equals(this, this));
    }

    @Test
    public void testEqualsNonEqualNumbers() {
        assertFalse(Objects.equals(80, 10));
    }

    @Test
    public void testDeepEqualsBasic() {
        assertTrue(Objects.deepEquals(this, this));
        assertFalse(Objects.deepEquals(80, 10));
        assertFalse(Objects.deepEquals(80, null));
    }

    @Test
    public void testDeepEqualsArrays() {
        final Supplier<Object> s = new Supplier<Object>() {
            @Override
            public Object get() {
                return new Object[] {
                        this, 1, 2, 3, "test",
                        new Integer[] {1, 2, 3}
                };
            }
        };
        assertTrue(Objects.deepEquals(s.get(), s.get()));
        assertFalse(Objects.deepEquals(s.get(), new Object[] {this, 1, 2, 3}));
    }

    @Test
    public void testHashCode() {
        Object value = new Random();
        assertEquals(value.hashCode(), Objects.hashCode(value));
    }

    @Test
    public void testHashRepeated() {
        Object value = new Random();
        int initial = Objects.hash(value, "test", 10, true, value, null, 50);
        assertEquals(initial, Objects.hash(value, "test", 10, true, value, null, 50));
        assertEquals(initial, Objects.hash(value, "test", 10, true, value, null, 50));
    }

    @Test
    public void testToString() {
        assertEquals("10", Objects.toString(10, ""));
    }

    @Test
    public void testToStringWithNullDefaults() {
        assertEquals("none", Objects.toString(null, "none"));
    }

    @Test
    public void testCompareLess() {
        int result = Objects.compare(10, 20, Functions.naturalOrder());
        assertEquals(-1, result);
    }

    @Test
    public void testCompareEquals() {
        int result = Objects.compare(20, 20, Functions.naturalOrder());
        assertEquals(0, result);
    }

    @Test
    public void testCompareGreater() {
        int result = Objects.compare(20, 10, Functions.naturalOrder());
        assertEquals(1, result);
    }

    @Test
    public void testCompareInt() {
        assertEquals(-1, Objects.compareInt(10, 20));

        assertEquals(0, Objects.compareInt(20, 20));

        assertEquals(1, Objects.compareInt(20, 10));
    }

    @Test
    public void testCompareLong() {
        assertEquals(-1, Objects.compareLong(100L, 10000L));

        assertEquals(0, Objects.compareLong(2000L, 2000L));

        assertEquals(1, Objects.compareLong(200000L, 10L));
    }

    @Test
    public void testRequireNonNull() {
        int result = Objects.requireNonNull(10);
        assertEquals(10, result);
    }

    @Test(expected = NullPointerException.class)
    public void testRequireNonNullWithException() {
        Objects.requireNonNull(null);
    }

    @Test
    public void testRequireNonNullWithExceptionAndMessage() {
        expectedException.expect(NullPointerException.class);
        expectedException.expectMessage("message");

        Objects.requireNonNull(null, "message");
    }

    @Test
    public void testRequireNonNullWithMessageSupplier() {
        Object result = Objects.requireNonNull("test", new Supplier<String>() {
            @Override
            public String get() {
                return "supplied message";
            }
        });
        assertEquals("test", result);
    }

    @Test
    public void testRequireNonNullWithMessageSupplierAndException() {
        expectedException.expect(NullPointerException.class);
        expectedException.expectMessage("supplied message");

        Objects.requireNonNull(null, new Supplier<String>() {
            @Override
            public String get() {
                return "supplied message";
            }
        });
    }

    @Test
    public void testRequireNonNullElse() {
        Object result = Objects.requireNonNullElse("a", "b");
        assertEquals("a", result);
    }

    @Test
    public void testRequireNonNullElseWithNullFirstArgument() {
        Object result = Objects.requireNonNullElse(null, "b");
        assertEquals("b", result);
    }

    @Test
    public void testRequireNonNullElseWithNullArguments() {
        expectedException.expect(NullPointerException.class);
        expectedException.expectMessage("defaultObj");

        Objects.requireNonNullElse(null, null);
    }

    @Test
    public void testRequireNonNullElseGet() {
        Object result = Objects.requireNonNullElseGet("a", new Supplier<String>() {
            @Override
            public String get() {
                return "b";
            }
        });
        assertEquals("a", result);
    }

    @Test
    public void testRequireNonNullElseGetWithNullFirstArgument() {
        Object result = Objects.requireNonNullElseGet(null, new Supplier<String>() {
            @Override
            public String get() {
                return "b";
            }
        });
        assertEquals("b", result);
    }

    @Test
    public void testRequireNonNullElseGetWithNullArguments() {
        expectedException.expect(NullPointerException.class);
        expectedException.expectMessage("supplier");

        Objects.requireNonNullElseGet(null, null);
    }

    @Test
    public void testRequireNonNullElseGetWithNullSupplied() {
        expectedException.expect(NullPointerException.class);
        expectedException.expectMessage("supplier.get()");

        Objects.requireNonNullElseGet(null, new Supplier<String>() {
            @Override
            public String get() {
                return null;
            }
        });
    }

    @Test
    public void testRequireNonNullElements() {
        Collection<Integer> col = Objects.requireNonNullElements(Arrays.asList(1, 2, 3, 4));
        assertThat(col, contains(1, 2, 3, 4));
    }

    @Test
    public void testRequireNonNullElementsWithNullCollection() {
        expectedException.expect(NullPointerException.class);
        Objects.requireNonNullElements(null);
    }

    @Test
    public void testRequireNonNullElementsWithNullElement() {
        expectedException.expect(NullPointerException.class);
        Objects.requireNonNullElements(Arrays.asList(1, 2, null, 4));
    }

    @Test
    public void testIsNull() {
        assertTrue(Objects.isNull(null));
        assertFalse(Objects.isNull(1));
    }

    @Test
    public void testNonNull() {
        assertFalse(Objects.nonNull(null));
        assertTrue(Objects.nonNull(1));
    }

    @Test
    public void testPrivateConstructor() throws Exception {
        assertThat(Objects.class, hasOnlyPrivateConstructors());
    }
}
