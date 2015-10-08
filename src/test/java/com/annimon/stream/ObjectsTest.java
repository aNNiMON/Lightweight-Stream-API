package com.annimon.stream;

import java.util.Comparator;
import java.util.Random;
import static org.junit.Assert.*;
import org.junit.Test;

/**
 *
 * @author aNNiMON
 */
public class ObjectsTest {
    
    @Test
    public void equals() {
        assertTrue(Objects.equals(this, this));
        assertFalse(Objects.equals(80, 10));
    }

    @Test
    public void testHashCode() {
        Object value = new Random();
        assertEquals(value.hashCode(), Objects.hashCode(value));
    }

    @Test
    public void hash() {
        Object value = new Random();
        int initial = Objects.hash(value, "test", 10, true, value, null, 50);
        assertEquals(initial, Objects.hash(value, "test", 10, true, value, null, 50));
        assertEquals(initial, Objects.hash(value, "test", 10, true, value, null, 50));
    }

    @Test
    public void testToString() {
        assertEquals("10", Objects.toString(10, ""));
        assertEquals("none", Objects.toString(null, "none"));
    }

    /**
     * Test of compare method, of class Objects.
     */
    @Test
    public void testCompare() {
        int result = Objects.compare(10, 20, new Comparator<Integer>() {

            @Override
            public int compare(Integer o1, Integer o2) {
                return Integer.compare(o1, o2);
            }
        });
        assertEquals(-1, result);
    }

    @Test(expected = NullPointerException.class)
    public void testRequireNonNull() {
        int result1 = Objects.requireNonNull(10);
        assertEquals(10, result1);
        
        int result2 = Objects.requireNonNull(10, "message");
        assertEquals(10, result2);
        
        Objects.requireNonNull(null);
        Objects.requireNonNull(null, "message");
    }
    
}
