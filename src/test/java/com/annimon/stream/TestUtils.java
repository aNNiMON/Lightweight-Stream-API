package com.annimon.stream;

import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * Additional methods for testing.
 */
public final class TestUtils {

    public static void testPrivateConstructor(Class<?> clazz) throws Exception {
        Constructor constructor = clazz.getDeclaredConstructor();
        constructor.setAccessible(true);
        
        assertTrue(Modifier.isPrivate(constructor.getModifiers()));
        assertNotNull(constructor.newInstance());
    }
    
    public static void assertArrayEqualsInAnyOrder(Object[] expecteds, Object[] actuals) {
        Arrays.sort(expecteds);
        Arrays.sort(actuals);
        assertArrayEquals(expecteds, actuals);
    }
}
