package com.annimon.stream.function;

import com.annimon.stream.Functions;
import static com.annimon.stream.test.hamcrest.CommonMatcher.hasOnlyPrivateConstructors;
import java.util.Locale;
import static org.junit.Assert.*;
import org.junit.Test;

/**
 * Tests {@code BiFunction}.
 * 
 * @see com.annimon.stream.function.BiFunction
 */
public class BiFunctionTest {
    
    private static final boolean TO_UPPER = true;
    private static final boolean TO_LOWER = false;
    private static final boolean INCREMENT = true;
    private static final boolean IDENTITY = false;
    
    @Test
    public void testApplyCharacterToString() {
        assertEquals("w", toString.apply('w', TO_LOWER));
        assertEquals("x", toString.apply('w', TO_UPPER));
    }
    
    @Test
    public void testApplyAsciiToString() {
        assertEquals("0", toString.apply((char)48, TO_LOWER));
        assertEquals("1", toString.apply((char)48, TO_UPPER));
    }
    
    @Test
    public void testApplyChangeCase() {
        assertEquals("JAVA", changeCase.apply("JAva", TO_UPPER));
        assertEquals("java", changeCase.apply("JAva", TO_LOWER));
    }
    
    @Test
    public void testAndThen() {
        BiFunction<Character, Boolean, Integer> function = BiFunction.Util.andThen(
                toString, Functions.stringToInteger());
        assertEquals(0, (int) function.apply('0', IDENTITY));
        assertEquals(1, (int) function.apply('0', INCREMENT));
    }

    @Test
    public void testReverse() {
        BiFunction<Boolean, String, String> function = BiFunction.Util.reverse(changeCase);
        assertEquals("JAVA", function.apply(TO_UPPER, "JAva"));
        assertEquals("java", function.apply(TO_LOWER, "JAva"));
    }
    
    @Test
    public void testPrivateConstructor() throws Exception {
        assertThat(BiFunction.Util.class, hasOnlyPrivateConstructors());
    }
    
    private static final BiFunction<Character, Boolean, String> toString = new BiFunction<Character, Boolean, String>() {
        @Override
        public String apply(Character value, Boolean increment) {
            final char character = (char) (increment ? value + 1 : value);
            return String.valueOf(character);
        }
    };
    
    private static final BiFunction<String, Boolean, String> changeCase = new BiFunction<String, Boolean, String>() {
        @Override
        public String apply(String value, Boolean flag) {
            if (flag) return value.toUpperCase(Locale.ENGLISH);
            return value.toLowerCase(Locale.ENGLISH);
        }
    };
    
}
