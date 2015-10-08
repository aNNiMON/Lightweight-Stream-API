package com.annimon.stream.function;

import java.util.Locale;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author aNNiMON
 */
public class BiFunctionTest {
    
    @Test
    public void apply() {
        assertEquals("w", toString.apply('w', false));
        assertEquals("x", toString.apply('w', true));
        assertEquals("0", toString.apply((char)48, false));
        assertEquals("1", toString.apply((char)48, true));
        assertEquals("JAVA", changeCase.apply("JAva", true));
        assertEquals("java", changeCase.apply("JAva", false));
    }
    
    @Test
    public void andThen() {
        BiFunction<Character, Boolean, Integer> function = BiFunction.Util.andThen(toString, new Function<String, Integer>() {

            @Override
            public Integer apply(String value) {
                return Integer.parseInt(value);
            }
        });
        assertEquals(0, (int) function.apply('0', false));
        assertEquals(1, (int) function.apply('0', true));
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
