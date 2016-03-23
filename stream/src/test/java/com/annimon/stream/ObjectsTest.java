package com.annimon.stream;

import static com.annimon.stream.test.CommonMatcher.hasOnlyPrivateConstructors;
import java.util.Random;
import static org.junit.Assert.*;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

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
