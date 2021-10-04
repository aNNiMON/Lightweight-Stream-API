package com.annimon.stream;

import static com.annimon.stream.test.hamcrest.CommonMatcher.hasOnlyPrivateConstructors;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import com.annimon.stream.function.Supplier;
import java.util.Arrays;
import java.util.Collection;
import java.util.Random;
import org.junit.Test;
import org.junit.function.ThrowingRunnable;

/**
 * Tests {@code Objects}.
 *
 * @see com.annimon.stream.Objects
 */
public class ObjectsTest {

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
        final Supplier<Object> s =
                new Supplier<Object>() {
                    @Override
                    public Object get() {
                        return new Object[] {this, 1, 2, 3, "test", new Integer[] {1, 2, 3}};
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
    public void testHashOnNull() {
        assertEquals(0, Objects.hash((Object[]) null));
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

    @SuppressWarnings("ObviousNullCheck")
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
        NullPointerException exc =
                assertThrows(
                        NullPointerException.class,
                        new ThrowingRunnable() {
                            @SuppressWarnings("ConstantConditions")
                            @Override
                            public void run() {
                                Objects.requireNonNull(null, "message");
                            }
                        });
        assertEquals("message", exc.getMessage());
    }

    @Test
    public void testRequireNonNullWithMessageSupplier() {
        Object result =
                Objects.requireNonNull(
                        "test",
                        new Supplier<String>() {
                            @Override
                            public String get() {
                                return "supplied message";
                            }
                        });
        assertEquals("test", result);
    }

    @Test
    public void testRequireNonNullWithMessageSupplierAndException() {
        NullPointerException exc =
                assertThrows(
                        NullPointerException.class,
                        new ThrowingRunnable() {
                            @SuppressWarnings("ConstantConditions")
                            @Override
                            public void run() {
                                Objects.requireNonNull(
                                        null,
                                        new Supplier<String>() {
                                            @Override
                                            public String get() {
                                                return "supplied message";
                                            }
                                        });
                            }
                        });
        assertEquals("supplied message", exc.getMessage());
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
        NullPointerException exc =
                assertThrows(
                        NullPointerException.class,
                        new ThrowingRunnable() {
                            @SuppressWarnings({"ConstantConditions", "ResultOfMethodCallIgnored"})
                            @Override
                            public void run() {
                                Objects.requireNonNullElse(null, null);
                            }
                        });
        assertEquals("defaultObj", exc.getMessage());
    }

    @Test
    public void testRequireNonNullElseGet() {
        Object result =
                Objects.requireNonNullElseGet(
                        "a",
                        new Supplier<String>() {
                            @Override
                            public String get() {
                                return "b";
                            }
                        });
        assertEquals("a", result);
    }

    @Test
    public void testRequireNonNullElseGetWithNullFirstArgument() {
        Object result =
                Objects.requireNonNullElseGet(
                        null,
                        new Supplier<String>() {
                            @Override
                            public String get() {
                                return "b";
                            }
                        });
        assertEquals("b", result);
    }

    @Test
    public void testRequireNonNullElseGetWithNullArguments() {
        NullPointerException exc =
                assertThrows(
                        NullPointerException.class,
                        new ThrowingRunnable() {
                            @SuppressWarnings({"ConstantConditions"})
                            @Override
                            public void run() {
                                Objects.requireNonNullElseGet(null, null);
                            }
                        });
        assertEquals("supplier", exc.getMessage());
    }

    @Test
    public void testRequireNonNullElseGetWithNullSupplied() {
        NullPointerException exc =
                assertThrows(
                        NullPointerException.class,
                        new ThrowingRunnable() {
                            @Override
                            public void run() {
                                Objects.requireNonNullElseGet(
                                        null,
                                        new Supplier<String>() {
                                            @Override
                                            public String get() {
                                                return null;
                                            }
                                        });
                            }
                        });
        assertEquals("supplier.get()", exc.getMessage());
    }

    @Test
    public void testRequireNonNullElements() {
        Collection<Integer> col = Objects.requireNonNullElements(Arrays.asList(1, 2, 3, 4));
        assertThat(col, contains(1, 2, 3, 4));
    }

    @SuppressWarnings("ConstantConditions")
    @Test(expected = NullPointerException.class)
    public void testRequireNonNullElementsWithNullCollection() {
        Objects.requireNonNullElements(null);
    }

    @Test(expected = NullPointerException.class)
    public void testRequireNonNullElementsWithNullElement() {
        Objects.requireNonNullElements(Arrays.asList(1, 2, null, 4));
    }

    @SuppressWarnings("ConstantConditions")
    @Test
    public void testIsNull() {
        assertTrue(Objects.isNull(null));
        assertFalse(Objects.isNull(1));
    }

    @SuppressWarnings("ConstantConditions")
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
