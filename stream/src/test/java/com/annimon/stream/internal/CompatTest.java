package com.annimon.stream.internal;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public final class CompatTest {

    @Test
    public void testNewArrayCompat() {
        String[] strings = new String[] {"abc", "def", "fff"};

        String[] copy = Compat.newArrayCompat(strings, 5);

        assertEquals(5, copy.length);
        assertEquals("abc", copy[0]);
        assertNull(copy[3]);

        String[] empty = new String[0];

        String[] emptyCopy = Compat.newArrayCompat(empty, 3);

        assertEquals(3, emptyCopy.length);

        emptyCopy = Compat.newArrayCompat(empty, 0);

        assertEquals(0, emptyCopy.length);
    }
}
