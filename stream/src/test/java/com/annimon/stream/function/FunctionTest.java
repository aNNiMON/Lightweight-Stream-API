package com.annimon.stream.function;

import com.annimon.stream.Functions;
import static com.annimon.stream.test.hamcrest.CommonMatcher.hasOnlyPrivateConstructors;
import java.io.IOException;
import java.util.Locale;
import static org.junit.Assert.*;
import org.junit.Test;

/**
 * Tests {@code Function}.
 *
 * @see com.annimon.stream.function.Function
 */
public class FunctionTest {

    @Test
    public void testApplyCharacterToString() {
        assertEquals("w", toString.apply('w'));
        assertEquals("0", toString.apply((char)48));
    }


    @Test
    public void testApplyToUpperCase() {
        assertEquals("JAVA", toUpperCase.apply("java"));
    }

    @Test
    public void testAndThen() {
        Function<Character, String> function = Function.Util.andThen(toString, toUpperCase);

        assertEquals("W", function.apply('w'));
        assertEquals("A", function.apply((char)65));
    }

    @Test
    public void testCompose() {
        Function<Character, String> function = Function.Util.compose(toUpperCase, toString);

        assertEquals("W", function.apply('w'));
        assertEquals("A", function.apply((char)65));
    }

    @Test
    public void testSafe() {
        Function<Boolean, Integer> function = Function.Util.safe(new ThrowableFunction<Boolean, Integer, Throwable>() {

            @Override
            public Integer apply(Boolean value) throws IOException {
                return unsafeFunction(value);
            }
        });

        assertEquals(10, (int) function.apply(false));
        assertNull(function.apply(true));
    }

    @Test
    public void testSafeWithResultIfFailed() {
        Function<Object, String> function = Function.Util.safe(new ThrowableFunction<Object, String, Throwable>() {

            @Override
            public String apply(Object value) {
                return value.toString();
            }
        }, "default");

        assertEquals("10", function.apply(10));
        assertEquals("default", function.apply(null));
    }

    @Test
    public void testPrivateConstructor() throws Exception {
        assertThat(Function.Util.class, hasOnlyPrivateConstructors());
    }

    private static int unsafeFunction(boolean throwException) throws IOException {
        if (throwException) {
            throw new IOException();
        }
        return 10;
    }

    private static final Function<Character, String> toString = Functions.<Character>convertToString();

    private static final Function<String, String> toUpperCase = new Function<String, String>() {
        @Override
        public String apply(String value) {
            return value.toUpperCase(Locale.ENGLISH);
        }
    };
}
