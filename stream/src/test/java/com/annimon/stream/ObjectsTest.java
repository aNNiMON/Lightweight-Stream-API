package com.annimon.stream;

import java.util.Random;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import static com.annimon.stream.test.hamcrest.CommonMatcher.hasOnlyPrivateConstructors;
import static org.hamcrest.Matchers.is;
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
    public ExpectedException expectedException = ExpectedException.none();

    @Test
    public void testEqualsThisObjects() {
        assertTrue(Objects.equals(this, this));
    }

    @Test
    public void testEqualsNonEqualNumbers() {
        assertFalse(Objects.equals(80, 10));
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
    public void testCompareDouble() {
        assertEquals(-1, Objects.compareDouble(0.01d, 0.1d));

        assertEquals(0, Objects.compareDouble(0.0d, 0.0d));

        assertEquals(1, Objects.compareDouble(0.2d, 0.1d));
    }

    @Test
    public void testCompareDouble_Specific() {
        assertThat(Objects.compareDouble(-0.0, 0.0), is(-1));

        assertThat(Objects.compareDouble(Double.NaN, Double.NaN), is(0));

        assertThat(Objects.compareDouble(Double.NaN, 80d), is(1));

        assertThat(Objects.compareDouble(Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY), is(-1));

        assertThat(Objects.compareDouble(Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY), is(0));
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
    public void testPrivateConstructor() throws Exception {
        assertThat(Objects.class, hasOnlyPrivateConstructors());
    }
}
