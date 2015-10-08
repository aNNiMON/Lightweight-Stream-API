package com.annimon.stream.function;

import java.util.Locale;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author aNNiMON
 */
public class FunctionTest {
    
    @Test
    public void apply() {
        assertEquals("w", toString.apply('w'));
        assertEquals("0", toString.apply((char)48));
        assertEquals("JAVA", toUpperCase.apply("java"));
    }
    
    @Test
    public void andThen() {
        Function<Character, String> function = Function.Util.andThen(toString, toUpperCase);
        assertEquals("W", function.apply('w'));
        assertEquals("A", function.apply((char)65));
    }
    
    private static final Function<Character, String> toString = new Function<Character, String>() {
        @Override
        public String apply(Character value) {
            return String.valueOf(value);
        }
    };
    
    private static final Function<String, String> toUpperCase = new Function<String, String>() {
        @Override
        public String apply(String value) {
            return value.toUpperCase(Locale.ENGLISH);
        }
    };
    
}
